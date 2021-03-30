package com.sstudio.core.data.source.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.sstudio.core.data.source.remote.network.ApiResponse
import com.sstudio.core.data.source.remote.response.*
import com.sstudio.core.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class RemoteDataSource(private val db: FirebaseFirestore) {

    fun getUser(userPhone: String): Flow<ApiResponse<UserResponse>> {
        return flow<ApiResponse<UserResponse>> {
            val docRef = db.collection("User").document(userPhone)
            val user = docRef.get().await()
            if (user.exists())
                emit(ApiResponse.Success(user.toObject(UserResponse::class.java) ?: UserResponse()))
            else
                emit(ApiResponse.Empty)
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

    fun setUser(user: User): Flow<ApiResponse<String>> =
        flow<ApiResponse<String>> {
            db.collection("User").document(user.phoneNumber)
                .set(user).await()
            emit(ApiResponse.Success(""))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun getHomeBanner(): Flow<ApiResponse<List<BannerResponse>>> {
        return flow<ApiResponse<List<BannerResponse>>> {
            val docRef = db.collection("Banner")
            val banner = docRef.get().await()
            val listBanner = ArrayList<BannerResponse>()
            for (bnr in banner) {
                listBanner.add(bnr.toObject(BannerResponse::class.java))
            }
            emit(ApiResponse.Success(listBanner))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

    fun getHomeLookBook(): Flow<ApiResponse<List<BannerResponse>>> =
        flow<ApiResponse<List<BannerResponse>>> {
            val docRef = db.collection("LookBook")
            val banner = docRef.get().await()
            val listBanner = ArrayList<BannerResponse>()
            for (bnr in banner) {
                listBanner.add(bnr.toObject(BannerResponse::class.java))
            }
            emit(ApiResponse.Success(listBanner))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun getAllPackage(): Flow<ApiResponse<List<PackageResponse>>> {
        return flow<ApiResponse<List<PackageResponse>>> {
            val docRef = db.collection("Package")
            val packages = docRef.get().await()
            val list = ArrayList<PackageResponse>()
            for (pkg in packages) {
                val packageResponse = pkg.toObject(PackageResponse::class.java)
                packageResponse.id = pkg.id
                list.add(packageResponse)
//                Log.d("mytag", "remote ${packageResponse}")
            }

            emit(ApiResponse.Success(list))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

    fun getAllCityOfGarage(): Flow<ApiResponse<List<CityResponse>>> {
        return flow<ApiResponse<List<CityResponse>>> {
            val docRef = db.collection("Garage")
            val city = docRef.get().await()
            val listCity = ArrayList<CityResponse>()
            for (cty in city) {
                val cityResponse = cty.toObject(CityResponse::class.java)
                cityResponse.id = cty.id
                listCity.add(cityResponse)
            }
//            Log.d("mytag", "remote ${city.get.}")
            emit(ApiResponse.Success(listCity))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    fun getBranchOfCity(city: String): Flow<ApiResponse<List<GarageResponse>>> {
        return callbackFlow<ApiResponse<List<GarageResponse>>> {
            val docRef = db.collection("Garage").document(city).collection("Branch")
            val listener = docRef.addSnapshotListener { snapshot, exception ->
                val list = ArrayList<GarageResponse>()
                snapshot?.let {
                    for (grg in it) {
                        val response = grg.toObject(GarageResponse::class.java)
                        response.id = grg.id
                        list.add(response)
                    }
                }
                offer(ApiResponse.Success(list))

                exception?.let {
                    offer(ApiResponse.Error(it.message.toString()))
                    cancel()
                }
            }
            awaitClose {
                listener.remove()
                cancel()
            }
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    fun getWorkingHours(): Flow<ApiResponse<List<WorkingHoursResponse>>> {
        return callbackFlow<ApiResponse<List<WorkingHoursResponse>>> {
            val docRef = db.collection("WorkingHours")
            val listener = docRef.addSnapshotListener { snapshot, exception ->
                val listWorkHours = ArrayList<WorkingHoursResponse>()
                snapshot?.let {
                    for (hours in snapshot) {
                        val response = hours.toObject(WorkingHoursResponse::class.java)
                        response.id = hours.id.toInt()
                        listWorkHours.add(response)
                    }
                }
                offer(ApiResponse.Success(listWorkHours))

                exception?.let {
                    offer(ApiResponse.Error(it.message.toString()))
                    cancel()
                }
            }
            awaitClose {
                listener.remove()
                cancel()
            }
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    fun getTimeSlotBooked(date: String, garageId: String): Flow<ApiResponse<List<String>>> {
        return callbackFlow<ApiResponse<List<String>>> {
            val docRef = db.collection("Booking").whereEqualTo("date", date)
                .whereEqualTo("garageId", garageId)
            val listener = docRef.addSnapshotListener { snapshot, exception ->
                val listIdTimeSlot = ArrayList<String>()
                snapshot?.let {
                    for (timeSlot in it) {
                        listIdTimeSlot.add(timeSlot["timeSlotId"].toString())
                    }
                }
                offer(ApiResponse.Success(listIdTimeSlot))

                exception?.let {
                    offer(ApiResponse.Error(it.message.toString()))
                    cancel()
                }
            }
            awaitClose {
                listener.remove()
                cancel()
            }
        }.flowOn(Dispatchers.IO)
    }

    fun setBooking(booking: BookingResponse): Flow<ApiResponse<String>> =
        flow<ApiResponse<String>> {
            db.collection("Booking")
                .add(booking).await()
            emit(ApiResponse.Success(""))
        }.catch {
            emit(ApiResponse.Error(it.message.toString()))
        }.flowOn(Dispatchers.IO)
}

