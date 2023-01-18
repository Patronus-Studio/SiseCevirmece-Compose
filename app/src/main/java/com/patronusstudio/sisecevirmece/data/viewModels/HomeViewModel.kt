package com.patronusstudio.sisecevirmece.data.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum
import com.patronusstudio.sisecevirmece.data.enums.TruthDareDefaultPackageEnum
import com.patronusstudio.sisecevirmece.data.model.AvatarModel
import com.patronusstudio.sisecevirmece.data.model.LevelModel
import com.patronusstudio.sisecevirmece.data.model.UserInfoModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.QuestionDbModel
import com.patronusstudio.sisecevirmece.data.repository.LocalRepository
import com.patronusstudio.sisecevirmece.data.repository.NetworkRepository
import com.patronusstudio.sisecevirmece.data.repository.local.PackageLocalRepository
import com.patronusstudio.sisecevirmece.data.repository.local.QuestionLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val localRepository: LocalRepository,
    private val packageLocalRepository: PackageLocalRepository,
    private val questionLocalRepository: QuestionLocalRepository,
) : ViewModel() {

    private val _loginError = MutableStateFlow("")
    val loginError: StateFlow<String> = _loginError

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _userGameInfoModel = MutableStateFlow<UserInfoModel?>(null)
    val userGameInfoModel: StateFlow<UserInfoModel?> = _userGameInfoModel

    private val _avatars = MutableStateFlow<List<AvatarModel>?>(null)
    val avatar: StateFlow<List<AvatarModel>?> = _avatars

    private val _levels = MutableStateFlow<List<LevelModel>?>(null)
    val levels: StateFlow<List<LevelModel>?> = _levels

    suspend fun getUserGameInfo(authToken: String) {
        _isLoading.value = true
        val result = networkRepository.getUserGameInfo(authToken)
        if (result.body() == null || result.body()!!.status != HttpStatusEnum.OK) {
            _isLoading.value = false
            _loginError.value = result.body()?.message
                ?: "Kullanıcı bilgisi çekilirken bir hata oluştu. Tekrar giriş yapın."
            return
        }
        _userGameInfoModel.value = result.body()!!.data
        _isLoading.value = false
    }

    suspend fun clearAuthToken(mContext: Context) {
        localRepository.removeUserToken(mContext)
    }

    fun getCurrentAvatar(): AvatarModel? {
        return _avatars.value?.firstOrNull {
            it.id.toString() == _userGameInfoModel.value?.currentAvatar
        }
    }

    suspend fun getAvatars() {
        _isLoading.value = true
        val avatarsResponse = networkRepository.getAvatars(_userGameInfoModel.value?.username ?: "")
        if (avatarsResponse.isSuccessful.not() || avatarsResponse.body()?.status != HttpStatusEnum.OK) {
            _errorMessage.value =
                avatarsResponse.body()?.message ?: "Avatarlar çekilirken bir hata oluştu."
            _isLoading.value = false
            return
        }
        _avatars.value = avatarsResponse.body()!!.data
        _isLoading.value = false
    }

    suspend fun getAllLevel() {
        _isLoading.value = true
        val levelResponse = networkRepository.getLevels()
        if (levelResponse.isSuccessful.not() || levelResponse.body()!!.status != HttpStatusEnum.OK) {
            _errorMessage.value =
                levelResponse.body()?.message ?: "Avatarlar çekilirken bir hata oluştu."
            _isLoading.value = false
            return
        }
        _levels.value = levelResponse.body()!!.data
        _isLoading.value = false
    }

    fun calculateNextLevelStarSize(list: List<LevelModel>): Int {
        val findedIndex = list.indexOfFirst {
            it.level == _userGameInfoModel.value?.level
        }
        return if (findedIndex == -1) 100
        else {
            _levels.value!!.get(findedIndex + 1).star
        }
    }

    suspend fun truthDareControl(context: Context) {
        _isLoading.value = true
        val truthPackage =
            packageLocalRepository.getPackageByName(
                context, TruthDareDefaultPackageEnum.TRUTH.getPackageName(context)
            )
        if (truthPackage == null) {
            val packageId = packageLocalRepository.addPackages(
                context, getDbModel(context, TruthDareDefaultPackageEnum.TRUTH)
            )
            val questionList =
                questionListToDbModel(context, TruthDareDefaultPackageEnum.TRUTH, packageId.toInt())
            questionLocalRepository.addQuestions(context, questionList)
        }
        val darePackage =
            packageLocalRepository.getPackageByName(context, context.getString(R.string.dare))
        if (darePackage == null) {
            val packageId = packageLocalRepository.addPackages(
                context, getDbModel(context, TruthDareDefaultPackageEnum.DARE)
            )
            val questionList =
                questionListToDbModel(context, TruthDareDefaultPackageEnum.DARE, packageId.toInt())
            questionLocalRepository.addQuestions(context, questionList)
        }
        _isLoading.value = false
    }

    private fun getDbModel(
        context: Context, truthDareDefaultPackageEnum: TruthDareDefaultPackageEnum
    ): PackageDbModel {
        return PackageDbModel(
            cloudPackageCategoryId = truthDareDefaultPackageEnum.cloudPackageCategoryId(),
            packageImage = truthDareDefaultPackageEnum.getPackageName(context)
                .toByteArray(),
            version = truthDareDefaultPackageEnum.getVersion(),
            packageName = truthDareDefaultPackageEnum.getPackageName(context),
            packageComment = truthDareDefaultPackageEnum.getPackageComment(context),
            createdTime = Calendar.getInstance().time.toString(),
            updatedTime = Calendar.getInstance().time.toString(),
        )
    }

    private fun questionListToDbModel(
        context: Context,
        truthDareDefaultPackageEnum: TruthDareDefaultPackageEnum,
        packageId: Int
    ): MutableList<QuestionDbModel> {
        val questions = mutableListOf<QuestionDbModel>()
        truthDareDefaultPackageEnum.getQuestions(context).forEach {
            questions.add(
                QuestionDbModel(
                    localPackagePrimaryId = packageId, question = it, isShowed = false
                )
            )
        }
        return questions
    }
}