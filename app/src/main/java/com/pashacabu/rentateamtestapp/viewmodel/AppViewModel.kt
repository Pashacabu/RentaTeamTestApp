package com.pashacabu.rentateamtestapp.viewmodel

import androidx.lifecycle.*
import com.pashacabu.rentateamtestapp.data.State
import com.pashacabu.rentateamtestapp.data.network.Network
import com.pashacabu.rentateamtestapp.data.db.AppDB
import com.pashacabu.rentateamtestapp.data.db.Converter
import com.pashacabu.rentateamtestapp.data.db.Users
import com.pashacabu.rentateamtestapp.data.network.DataItem
import com.pashacabu.rentateamtestapp.data.network.NetworkResponse
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import kotlin.Exception

class AppViewModel(
    private val db: AppDB
) : ViewModel() {

    private val network: Network.NetworkInterface = Network().apiService
    private val rXNetwork = Network().rxService

    private var currentPage = 1
    private var totalPages = 0
    private var currentList: MutableList<DataItem?>? = mutableListOf()
    private val converter = Converter()

    private val mutableUsersList: MutableLiveData<List<DataItem?>?> = MutableLiveData()
    private val mutableState: MutableLiveData<State> = MutableLiveData()
    private val mutableDetails: MutableLiveData<DataItem> = MutableLiveData()

    val liveUsersList: LiveData<List<DataItem?>?> get() = mutableUsersList
    val liveState: LiveData<State> get() = mutableState
    val liveDetails: LiveData<DataItem> get() = mutableDetails


    fun loadData(connected: Boolean) {

        if (connected) {
            viewModelScope.launch {
                mutableState.postValue(State.Loading)
                try {
                    val data = network.getUsers(currentPage)
                    currentList = data.data as MutableList<DataItem?>?
                    mutableUsersList.postValue(currentList)
                    currentPage = data.page ?: 0
                    totalPages = data.totalPages ?: 0
                    mutableState.postValue(State.Loaded)
                    currentList?.let { converter.nrListToDBList(it) }?.let {
                        db.usersDAO().insertUsers(
                            it
                        )
                    }
                } catch (e: Exception) {
                    mutableState.postValue(State.Error)
                    e.printStackTrace()
                }

            }
        } else {
            viewModelScope.launch {
                mutableUsersList.postValue(
                    converter.dbListToNRList(
                        db.usersDAO().getUsers()
                    )
                )
            }
        }


    }

    fun loadDifferently(connected: Boolean) {
        mutableState.postValue(State.Loading)
        if (connected) {
            rXNetwork.getUsers(currentPage)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(object : MaybeObserver<NetworkResponse> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onError(e: Throwable) {
                        mutableState.postValue(State.Error)
                    }

                    override fun onComplete() {
                    }

                    override fun onSuccess(t: NetworkResponse) {
                        if (t.data != null) {
                            currentList?.addAll(t.data)
                        }
                        mutableUsersList.postValue(currentList)
                        mutableState.postValue(State.Loaded)
                        currentPage = t.page ?: 0
                        totalPages = t.totalPages ?: 0
                        saveToDB(currentList)
                    }
                })
        } else {
            loadFromDB()
        }
    }

    fun loadMoreDifferently(connected: Boolean) {
        if (connected) {
            if (currentPage < totalPages) {
                currentPage += 1
                loadDifferently(connected)
            } else {
                mutableState.postValue(State.ListEnd)
            }
        } else {
            mutableState.postValue(State.NoConnection)
        }

    }

    private fun saveToDB(list: MutableList<DataItem?>?) {
        val users = converter.nrListToDBList(list)
        db.usersDAO().rxInsertUsers(users)
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe(
                object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onComplete() {
                    }

                    override fun onError(e: Throwable) {
                        mutableState.postValue(State.Error)
                    }

                }
            )
    }

    private fun loadFromDB() {
        db.usersDAO().rxGetUsers()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : MaybeObserver<List<Users>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onSuccess(t: List<Users>) {
                    if (t.isNullOrEmpty()) {
                        mutableState.postValue(State.Error)
                    } else {
                        val res = converter.dbListToNRList(t)
                        mutableUsersList.postValue(res)
                        currentList = res
                        mutableState.postValue(State.Loaded)
                    }

                }

                override fun onError(e: Throwable) {
                    mutableState.postValue(State.Error)
                }

                override fun onComplete() {

                }
//
            }
            )
    }


    fun loadMore(connected: Boolean) {
        if (connected) {
            viewModelScope.launch {
                mutableState.postValue(State.Loading)
                try {
                    if (currentPage < totalPages) {
                        val newData = network.getUsers(currentPage + 1)
                        currentPage = newData.page ?: 0
                        totalPages = newData.totalPages ?: 0
                        val newList: MutableList<DataItem?>? = mutableListOf()
                        currentList?.let { newList?.addAll(it) }
                        newData.data?.let { newList?.addAll(it) }
                        mutableUsersList.postValue(newList)
                        currentList = newList
                        mutableState.postValue(State.Loaded)
                        currentList?.let { converter.nrListToDBList(it) }?.let {
                            db.usersDAO().insertUsers(
                                it
                            )
                        }
                    } else {
                        mutableState.postValue(State.ListEnd)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    mutableState.postValue(State.Error)
                }
            }
        } else {
            mutableState.postValue(State.NoConnection)
        }

    }

    fun refresh(connected: Boolean) {
        if (connected) {
            viewModelScope.launch {
                mutableState.postValue(State.Loading)
                try {
                    val refreshedList = network.getUsers(1)
                    currentPage = 1
                    totalPages = refreshedList.totalPages ?: 0
                    mutableUsersList.postValue(refreshedList.data)
                    currentList?.clear()
                    refreshedList.data?.let { currentList?.addAll(it) }
                    mutableState.postValue(State.Loaded)
                } catch (e: Exception) {
                    e.printStackTrace()
                    mutableState.postValue(State.Error)
                }

            }
        } else {
            viewModelScope.launch {
                mutableUsersList.postValue(
                    converter.dbListToNRList(
                        db.usersDAO().getUsers()
                    )
                )
                mutableState.postValue(State.Loaded)
            }
        }


    }

    fun refreshDifferently(connected: Boolean) {
        currentList?.clear()
        currentPage = 1
//        db.usersDAO().rxDeleteAll()
//            .subscribeOn(Schedulers.io())
//            .observeOn(Schedulers.io())
//            .subscribe()
        loadDifferently(connected)

    }

    fun showUserDetails(user: DataItem) {
        mutableDetails.postValue(user)
    }


}