package com.patronusstudio.sisecevirmece.data.viewModels

import android.app.Application
import android.os.Build
import androidx.lifecycle.ViewModel
import com.patronusstudio.sisecevirmece.BuildConfig
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum
import com.patronusstudio.sisecevirmece.data.enums.SampleBackgroundEnum
import com.patronusstudio.sisecevirmece.data.enums.SampleBottleEnum
import com.patronusstudio.sisecevirmece.data.enums.TruthDareDefaultPackageEnum
import com.patronusstudio.sisecevirmece.data.model.AvatarModel
import com.patronusstudio.sisecevirmece.data.model.LevelModel
import com.patronusstudio.sisecevirmece.data.model.UserCommentRequest
import com.patronusstudio.sisecevirmece.data.model.UserInfoModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.BackgroundDbModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.BottleDbModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.PackageDbModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.QuestionDbModel
import com.patronusstudio.sisecevirmece.data.repository.LocalRepository
import com.patronusstudio.sisecevirmece.data.repository.NetworkRepository
import com.patronusstudio.sisecevirmece.data.repository.local.BackgroundLocalRepository
import com.patronusstudio.sisecevirmece.data.repository.local.BottleLocalRepository
import com.patronusstudio.sisecevirmece.data.repository.local.PackageLocalRepository
import com.patronusstudio.sisecevirmece.data.repository.local.QuestionLocalRepository
import com.patronusstudio.sisecevirmece.data.utils.toBitmapArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
    private val networkRepository: NetworkRepository,
    private val localRepository: LocalRepository,
    private val packageLocalRepository: PackageLocalRepository,
    private val questionLocalRepository: QuestionLocalRepository,
    private val bottleLocalRepository: BottleLocalRepository,
    private val backgroundLocalRepository: BackgroundLocalRepository
) : ViewModel() {

    private val _loginError = MutableStateFlow("")
    val loginError = _loginError.asStateFlow()

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

    suspend fun clearAuthToken() {
        localRepository.removeUserToken(application.applicationContext)
    }

    fun getCurrentAvatar(): AvatarModel? {
        return _avatars.value?.firstOrNull {
            it.id.toString() == _userGameInfoModel.value?.currentAvatar
        }
    }

    fun setCurrentAvatar(avatarId: Int) {
        _userGameInfoModel.value!!.currentAvatar = avatarId.toString()
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

    suspend fun truthDareControl() {
        _isLoading.value = true
        val truthPackage =
            packageLocalRepository.getPackageByName(
                TruthDareDefaultPackageEnum.TRUTH.getPackageName(application.applicationContext)
            )
        if (truthPackage == null) {
            packageLocalRepository.addPackages(
                getDbModel(TruthDareDefaultPackageEnum.TRUTH)
            )
            val questionList =
                questionListToDbModel(TruthDareDefaultPackageEnum.TRUTH)
            questionLocalRepository.addQuestions(questionList)
        }
        val darePackage =
            packageLocalRepository.getPackageByName(application.applicationContext.getString(R.string.dare))
        if (darePackage == null) {
            packageLocalRepository.addPackages(
                getDbModel(TruthDareDefaultPackageEnum.DARE)
            )
            val questionList =
                questionListToDbModel(TruthDareDefaultPackageEnum.DARE)
            questionLocalRepository.addQuestions(questionList)
        }
        _isLoading.value = false
    }

    private suspend fun getDbModel(
        truthDareDefaultPackageEnum: TruthDareDefaultPackageEnum
    ): PackageDbModel {
        val image = withContext(Dispatchers.IO) {
            truthDareDefaultPackageEnum.getImageId().toBitmapArray(application.applicationContext)
        }
        return PackageDbModel(
            cloudPackageCategoryId = truthDareDefaultPackageEnum.getPackageCategoryId(),
            packageImage = image,
            version = truthDareDefaultPackageEnum.getVersion(),
            packageName = truthDareDefaultPackageEnum.getPackageName(application.applicationContext),
            packageComment = truthDareDefaultPackageEnum.getPackageComment(application.applicationContext),
            createdTime = Calendar.getInstance().time.toString(),
            updatedTime = Calendar.getInstance().time.toString(),
        )
    }

    private fun questionListToDbModel(
        truthDareDefaultPackageEnum: TruthDareDefaultPackageEnum
    ): MutableList<QuestionDbModel> {
        val questions = mutableListOf<QuestionDbModel>()
        truthDareDefaultPackageEnum.getQuestions(application.applicationContext).forEach {
            questions.add(
                QuestionDbModel(
                    localPackagePrimaryId = truthDareDefaultPackageEnum.getPackageCategoryId(),
                    question = it,
                    isShowed = false
                )
            )
        }
        return questions
    }

    suspend fun bottleControl() {
        _isLoading.value = true
        val bottleSize = bottleLocalRepository.getBottles()
        if (bottleSize.isEmpty().not()) {
            _isLoading.value = false
            return
        } else {
            val bottles = mutableListOf<BottleDbModel>()
            SampleBottleEnum.values().forEach {
                val image = withContext(Dispatchers.IO) {
                    it.getImageId().toBitmapArray(application.applicationContext)
                }
                val tempModel = BottleDbModel(
                    packageImage = image,
                    bottleName = it.getBottleName(application.applicationContext),
                    isActive = it == SampleBottleEnum.ORJINAL
                )
                bottles.add(tempModel)
            }
            bottleLocalRepository.insertBottles(bottles)
            _isLoading.value = false
        }
    }

    suspend fun backgroundControl() {
        _isLoading.value = true
        val backgroundsSize = backgroundLocalRepository.getBackgrounds()
        if (backgroundsSize.isEmpty().not()) {
            _isLoading.value = false
            return
        } else {
            val backgrounds = mutableListOf<BackgroundDbModel>()
            SampleBackgroundEnum.values().forEach {
                val image = withContext(Dispatchers.IO) {
                    it.getImageId().toBitmapArray(application.applicationContext)
                }
                val tempModel = BackgroundDbModel(
                    packageImage = image,
                    backgroundName = it.getBottleName(application.applicationContext),
                    isActive = it == SampleBackgroundEnum.ORJINAL
                )
                backgrounds.add(tempModel)
            }
            backgroundLocalRepository.insertBackgrounds(backgrounds)
            _isLoading.value = false
        }
    }

    suspend fun updateAvatar(avatarId: Int) {
        _isLoading.value = true
        networkRepository.updateAvatar(_userGameInfoModel.value?.username ?: "", avatarId)
        _isLoading.value = false
    }

    suspend fun addNewComment(comment: String, starSize: Float) {
        _isLoading.value = true
        val userCommentRequest = UserCommentRequest(
            appVersion = BuildConfig.VERSION_NAME,
            comment = comment,
            deviceModel = Build.MODEL + " - " + Build.MANUFACTURER,
            deviceType = "GMS",
            sendDate = System.currentTimeMillis().toString(),
            starCount = starSize,
            username = _userGameInfoModel.value?.username ?: ""
        )
        val result = networkRepository.addNewComment(userCommentRequest)
        if (result.body()?.status != HttpStatusEnum.OK) {
            _errorMessage.value = result.body()?.message
                ?: application.applicationContext.getString(R.string.getting_some_error)
        }
        _isLoading.value = false
    }

}