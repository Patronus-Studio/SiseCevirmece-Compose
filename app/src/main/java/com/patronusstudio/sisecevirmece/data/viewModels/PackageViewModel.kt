package com.patronusstudio.sisecevirmece.data.viewModels

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import com.patronusstudio.sisecevirmece.R
import com.patronusstudio.sisecevirmece.data.enums.HttpStatusEnum
import com.patronusstudio.sisecevirmece.data.enums.PackageDetailCardBtnEnum
import com.patronusstudio.sisecevirmece.data.enums.SelectableEnum
import com.patronusstudio.sisecevirmece.data.model.BasePackageModel
import com.patronusstudio.sisecevirmece.data.model.PackageCategoryModel
import com.patronusstudio.sisecevirmece.data.model.PackageModel
import com.patronusstudio.sisecevirmece.data.model.dbmodel.QuestionDbModel
import com.patronusstudio.sisecevirmece.data.repository.local.PackageLocalRepository
import com.patronusstudio.sisecevirmece.data.repository.local.QuestionLocalRepository
import com.patronusstudio.sisecevirmece.data.repository.network.PackageNetworkRepository
import com.patronusstudio.sisecevirmece.data.utils.downloadImage
import com.patronusstudio.sisecevirmece.data.utils.toByteArrray
import com.patronusstudio.sisecevirmece.data.utils.toPackageDbModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PackageViewModel @Inject constructor(
    private val packageNetworkRepository: PackageNetworkRepository,
    private val packageLocalRepository: PackageLocalRepository,
    private val questionLocalRepository: QuestionLocalRepository
) : BaseViewModel() {

    private val _categories = MutableStateFlow<List<PackageCategoryModel>>(listOf())
    val categories: StateFlow<List<PackageCategoryModel>> = _categories

    var packages = mutableStateListOf<PackageModel>()

    private val _currentPackage = MutableStateFlow<PackageModel?>(null)
    val currentPackage: StateFlow<PackageModel?> get() = _currentPackage

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun setPackageModel(packageModel: PackageModel) {
        _currentPackage.value = packageModel
    }

    suspend fun getPackageCategories() {
        _isLoading.value = true
        val categories = packageNetworkRepository.getPackageCategories()
        if (categories.isSuccessful.not() || categories.body() == null || categories.body()?.status != HttpStatusEnum.OK) {
            _errorMessage.value =
                categories.body()?.message ?: "Bir hatayla karşılaşıldı."
        }
        _categories.value = (categories.body()?.packageCategoryModel
            ?: arrayListOf()) as MutableList<PackageCategoryModel>
        _isLoading.value = false
    }

    fun clickedBtn(clickedItemId: Int) {
        val findedActiveBtnIndex = _categories.value.indexOfFirst {
            it.isSelected == SelectableEnum.YES
        }
        if (findedActiveBtnIndex == -1 || findedActiveBtnIndex == clickedItemId) return

        val tempList = mutableListOf<PackageCategoryModel>()
        _categories.value.forEach {
            if (findedActiveBtnIndex.toDouble() == it.id - 1) tempList.add(it.copy(isSelected = SelectableEnum.NO))
            else if (clickedItemId.toDouble() == it.id - 1) tempList.add(it.copy(isSelected = SelectableEnum.YES))
            else tempList.add(it.copy())
        }
        _categories.value = tempList
    }

    suspend fun getPackageFromCategory(context: Context, clickedItemId: Int) {
        _isLoading.value = true
        val networkPackages = packageNetworkRepository.getPackageCategories(clickedItemId)
        if (networkPackages.isSuccessful.not() || networkPackages.body() == null || networkPackages.body()?.status != HttpStatusEnum.OK) {
            _errorMessage.value =
                networkPackages.body()?.message?.toString() ?: "Bir hatayla karşılaşıldı."
        }
        val localPackages = packageLocalRepository.getPackages(context)
        val mutableMap = mutableMapOf<Int, MutableList<BasePackageModel>>()
        networkPackages.body()?.packages?.forEach {
            it.packageStatu = PackageDetailCardBtnEnum.NEED_DOWNLOAD
            mutableMap[it.id] = mutableListOf(it)
        }
        localPackages.forEach { localModel ->
            if (mutableMap[localModel.cloudPackageCategoryId].isNullOrEmpty().not()) {
                val networkModel = mutableMap[localModel.cloudPackageCategoryId]!!.first()
                val newImageId =
                    if (localModel.version < networkModel.version) R.drawable.update else R.drawable.tick
                val newPackageStatu = when {
                    localModel.version == networkModel.version -> PackageDetailCardBtnEnum.REMOVABLE
                    localModel.version < networkModel.version -> PackageDetailCardBtnEnum.NEED_UPDATE
                    else -> PackageDetailCardBtnEnum.NEED_DOWNLOAD
                }
                val newPackageModel = (networkModel as PackageModel).copy(
                    imageId = if (newImageId != networkModel.imageId) newImageId else networkModel.imageId,
                    packageStatu = if (newPackageStatu != networkModel.packageStatu) newPackageStatu else networkModel.packageStatu
                )
                mutableMap[localModel.cloudPackageCategoryId] = mutableListOf(newPackageModel)
            }
        }
        packages.clear()
        mutableMap.values.forEach {
            packages.add(it[0] as PackageModel)
        }
        _isLoading.value = false
    }

    suspend fun removePackage(context: Context) {
        _isLoading.value = true
        val dbPackageModel =
            packageLocalRepository.getPackageOnCloudPackageCategoryId(
                context,
                _currentPackage.value!!.id
            )
        packageLocalRepository.removePackage(context, dbPackageModel.primaryId)
        questionLocalRepository.removeQuestions(context, dbPackageModel.primaryId)
        delay(500L)
        setPackageStatu(PackageControlStatu.REMOVED)
        updateModelOnList()
        _isLoading.value = false
    }

    suspend fun downloadPackage(context: Context) {
        _isLoading.value = true
        val byteArray = downloadImage(context, _currentPackage.value!!.imageUrl).toByteArrray()
        val packageDbModel = _currentPackage.value!!.toPackageDbModel(byteArray)
        val packageId = packageLocalRepository.addPackages(context, packageDbModel)
        insertQuestions(context, packageId.toInt())
        setPackageStatu(PackageControlStatu.DOWNLOADED)
        updateModelOnList()
        _isLoading.value = false
    }

    suspend fun updatePackage(context: Context) {
        _isLoading.value = true
        val byteArray = downloadImage(context, _currentPackage.value!!.imageUrl).toByteArrray()
        val dbPackageModel =
            packageLocalRepository.getPackageOnCloudPackageCategoryId(
                context,
                _currentPackage.value!!.id
            )
        val copiedDbPackageModel = dbPackageModel.copy(
            cloudPackageCategoryId = _currentPackage.value!!.id,
            packageImage = byteArray,
            version = _currentPackage.value!!.version,
            packageName = _currentPackage.value!!.packageName,
            packageComment = _currentPackage.value!!.packageComment,
            updatedTime = _currentPackage.value!!.updatedTime
        )
        packageLocalRepository.updatePackage(context, copiedDbPackageModel)
        questionLocalRepository.removeQuestions(context, dbPackageModel.primaryId)
        insertQuestions(context, copiedDbPackageModel.primaryId)
        setPackageStatu(PackageControlStatu.UPDATED)
        updateModelOnList()
        _isLoading.value = false
    }

    private suspend fun insertQuestions(context: Context, packagePrimaryId: Int) {
        val questions = mutableListOf<QuestionDbModel>().apply {
            _currentPackage.value!!.questions.forEach {
                this.add(
                    QuestionDbModel(
                        localPackagePrimaryId = packagePrimaryId,
                        question = it,
                        isShowed = false
                    )
                )
            }
        }
        questionLocalRepository.addQuestions(context, questions)
    }

    private fun setPackageStatu(packageStatuParam: PackageControlStatu) {
        var imageId = _currentPackage.value!!.imageId
        var packageStatu = _currentPackage.value!!.packageStatu
        when (packageStatuParam) {
            PackageControlStatu.DOWNLOADED -> {
                imageId = R.drawable.tick
                packageStatu = PackageDetailCardBtnEnum.REMOVABLE
            }
            PackageControlStatu.UPDATED -> {
                imageId = R.drawable.tick
                packageStatu = PackageDetailCardBtnEnum.REMOVABLE
            }
            PackageControlStatu.REMOVED -> {
                imageId = null
                packageStatu = PackageDetailCardBtnEnum.NEED_DOWNLOAD
            }
        }
        val newPackage = _currentPackage.value!!.copy(
            packageStatu = packageStatu,
            imageId = if (imageId != _currentPackage.value!!.imageId) imageId else _currentPackage.value!!.imageId
        )
        _currentPackage.value = newPackage
    }

    private fun updateModelOnList() {
        val findedIndex = packages.indexOfFirst {
            it.id == currentPackage.value?.id
        }
        if (findedIndex != -1) {
            packages[findedIndex] = _currentPackage.value!!
        }
    }

    private enum class PackageControlStatu {
        DOWNLOADED, UPDATED, REMOVED
    }
}