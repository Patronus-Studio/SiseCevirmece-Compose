package com.patronusstudio.sisecevirmece2.data.viewModels

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import com.patronusstudio.sisecevirmece2.R
import com.patronusstudio.sisecevirmece2.data.abstarcts.BottleRoomDb
import com.patronusstudio.sisecevirmece2.data.enums.HttpStatusEnum
import com.patronusstudio.sisecevirmece2.data.enums.SelectableEnum
import com.patronusstudio.sisecevirmece2.data.model.PackageCategoryModel
import com.patronusstudio.sisecevirmece2.data.model.QuestionModel
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece2.data.model.dbmodel.QuestionDbModel
import com.patronusstudio.sisecevirmece2.data.repository.LocalRepository
import com.patronusstudio.sisecevirmece2.data.repository.NetworkRepository
import com.patronusstudio.sisecevirmece2.data.repository.network.PackageNetworkRepository
import com.patronusstudio.sisecevirmece2.data.utils.getCurrentTime
import com.patronusstudio.sisecevirmece2.data.utils.removeModelOnList
import com.patronusstudio.sisecevirmece2.data.utils.toByteArrray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddCategoriesScreenViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val packageNetworkRepository: PackageNetworkRepository,
    private val localRepository: LocalRepository,
) : BaseViewModel() {

    private val _questionList = MutableStateFlow(
        mutableStateListOf(QuestionModel(1, "", "", null))
    )
    val questionList: StateFlow<List<QuestionModel>> get() = _questionList

    private val _packageName = MutableStateFlow("")
    val packageName: StateFlow<String> get() = _packageName

    private val _selectedImage = MutableStateFlow<Bitmap?>(null)
    val selectedImage: StateFlow<Bitmap?> get() = _selectedImage

    private val _packageComment = MutableStateFlow("")
    val packageComment: StateFlow<String> get() = _packageComment

    private val _packageCategoryModel = MutableStateFlow<PackageCategoryModel?>(null)
    val packageCategoryModel: StateFlow<PackageCategoryModel?> get() = _packageCategoryModel

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage

    private val _categories = MutableStateFlow<List<PackageCategoryModel>>(listOf())
    val categories: StateFlow<List<PackageCategoryModel>> get() = _categories

    fun updateQuestionModelText(questionModel: QuestionModel, newText: String) {
        questionModel.question = newText
        _questionList.value[questionModel.id - 1] = questionModel
    }

    fun removeQuestionModel(questionModel: QuestionModel) {
        val result = questionModel.removeModelOnList(_questionList.value)
        _questionList.value.clear()
        _questionList.value.addAll(result)
    }

    fun addNewQuestionModel() {
        _questionList.value.add(QuestionModel(_questionList.value.last().id + 1, "", "", null))
    }

    fun setPackageName(name: String) {
        _packageName.value = name
    }

    fun setPackageComment(comment: String) {
        _packageComment.value = comment
    }

    fun setPackageCategory(packageCategoryModel: PackageCategoryModel) {
        _packageCategoryModel.value = packageCategoryModel
    }

    fun setBitmap(bitmap: Bitmap) {
        _selectedImage.value = bitmap
    }

    suspend fun saveQuestions(context: Context) {
        val listIsEmpty = listEmptyControl()
        if (listIsEmpty) {
            _errorMessage.value = context.getString(R.string.package_question_empty_error_message)
            return
        }
        if (_packageName.value.isEmpty()) {
            _errorMessage.value = context.getString(R.string.enter_package_name)
            return
        }
        if (_packageComment.value.isEmpty()) {
            _errorMessage.value = context.getString(R.string.enter_package_comment)
            return
        }
        if (_packageCategoryModel.value == null) {
            _errorMessage.value = context.getString(R.string.select_package_category)
            return
        }
        _isLoading.value = true
        saveDb(context)
        clearAllContent()
        _isLoading.value = false
    }

    private suspend fun saveDb(context: Context) {
        val packageModel = PackageDbModel(
            primaryId = 0,
            cloudPackageCategoryId = _packageCategoryModel.value?.id?.toInt() ?: -1,
            packageImage = selectedImage.value!!.toByteArrray(),
            version = 1,
            packageName = _packageName.value,
            packageComment = _packageComment.value,
            createdTime = getCurrentTime(),
            updatedTime = getCurrentTime()
        )
        val packageId = withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(context).getPackageRoomDao().insertPackage(packageModel)
        }
        val tempQuestionList = mutableListOf<QuestionDbModel>()
        _questionList.value.forEach {
            tempQuestionList.add(
                QuestionDbModel(
                    localPackagePrimaryId = packageId.toInt(),
                    question = it.question,
                    isShowed = 0, correctAnswer = it.correctAnswer, punishment = it.punishment
                )
            )
        }
        withContext(Dispatchers.IO) {
            BottleRoomDb.getInstance(context).getQuestionRoomDao()
                .insertQuestions(tempQuestionList.toList())
        }
    }

    private fun clearAllContent() {
        _packageComment.value = ""
        _packageName.value = ""
        _packageCategoryModel.value = null
        _selectedImage.value = null
        _questionList.value = mutableStateListOf(QuestionModel(1, "asdasd", "", null))
    }

    private fun listEmptyControl(): Boolean {
        val notEmptyQuestionSize = _questionList.value.filter {
            it.question.isNotEmpty() && it.question.isNotBlank()
        }.size
        return notEmptyQuestionSize < 10
    }

    fun clearErrorMessage() {
        _errorMessage.value = ""
    }

    suspend fun getPackageCategories() {
        _isLoading.value = true
        val result = packageNetworkRepository.getPackageCategories()
        val body = result.body()
        if (body == null || body.status != HttpStatusEnum.OK) {
            _errorMessage.value = body?.message ?: "Bir hatayla karşılaşıldı"
            _isLoading.value = false
            return
        }
        _categories.value = body.packageCategoryModel.filter {
            it.isUserSelectableWhenCreatingPackage == SelectableEnum.YES
        }
        _isLoading.value = false
    }


}