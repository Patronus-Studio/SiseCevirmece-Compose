package com.patronusstudio.sisecevirmece.data.repository.network

import com.patronusstudio.sisecevirmece.data.model.PackageCategoryResponseModel
import com.patronusstudio.sisecevirmece.data.model.PackageResponseModel
import com.patronusstudio.sisecevirmece.data.objects.RetrofitObjects
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

}