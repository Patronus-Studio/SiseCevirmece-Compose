package com.patronusstudio.sisecevirmece2.data.repository.network

import com.patronusstudio.sisecevirmece2.data.model.BaseResponse
import com.patronusstudio.sisecevirmece2.data.model.PackageCategoryResponseModel
import com.patronusstudio.sisecevirmece2.data.model.PackageResponseModel
import com.patronusstudio.sisecevirmece2.data.model.SampleResponse
import com.patronusstudio.sisecevirmece2.data.objects.RetrofitObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class PackageNetworkRepository @Inject constructor() {

    suspend fun getPackageCategories(id: Int): Response<PackageResponseModel> {
        return withContext(Dispatchers.IO) {
            RetrofitObjects.getApi().getPackageByCategoryName(id)
        }
    }

    suspend fun getPackageCategories(): Response<PackageCategoryResponseModel> {
        return withContext(Dispatchers.IO) {
            RetrofitObjects.getApi().getAllPackageCategories()
        }
    }

    suspend fun updatePackageNumberOfDownload(packageId: Int): Response<SampleResponse> {
        return withContext(Dispatchers.IO) {
            RetrofitObjects.getApi().updatePackageNumberOfDownload(packageId)
        }
    }

}