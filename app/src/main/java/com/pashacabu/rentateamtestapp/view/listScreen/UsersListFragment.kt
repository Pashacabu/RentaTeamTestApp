package com.pashacabu.rentateamtestapp.view.listScreen

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pashacabu.rentateamtestapp.R
import com.pashacabu.rentateamtestapp.data.db.AppDB
import com.pashacabu.rentateamtestapp.data.network.DataItem
import com.pashacabu.rentateamtestapp.view.detailsScreen.DetailsFragment
import com.pashacabu.rentateamtestapp.viewmodel.APPViewModelFactory
import com.pashacabu.rentateamtestapp.viewmodel.AppViewModel
import com.pashacabu.rentateamtestapp.data.State

class UsersListFragment : Fragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    private val userSelector =object : UserOnTouchInterface {
        override fun onSelectUser(user: DataItem) {
            viewModel.showUserDetails(user)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, DetailsFragment(), LIST_TAG)
                .addToBackStack(LIST_TAG)
                .commit()
        }
    }

    private val adapter = UsersAdapter(userSelector)
    private lateinit var viewModel : AppViewModel

    private var toast : Toast? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = createDB()
        viewModel = ViewModelProvider(requireActivity(), APPViewModelFactory(db)).get(AppViewModel::class.java)
        findViews(view)
        observeViewModel(viewModel)
//        viewModel.loadData(checkConnection())
        viewModel.loadDifferently(checkConnection())
    }

    private fun createDB() : AppDB{
       return AppDB.createDB(requireContext())
    }

    private fun checkConnection(): Boolean{
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    private fun observeViewModel(viewModel: AppViewModel) {

        viewModel.liveUsersList.observe(this.viewLifecycleOwner, {
            adapter.submitList(it)
        })

        viewModel.liveState.observe(this.viewLifecycleOwner, {
            when (it){
                State.Loading -> swipeRefresh.isRefreshing = true
                State.Loaded, State.Error, State.ListEnd, State.NoConnection -> {
                    swipeRefresh.isRefreshing = false
                    toast?.cancel()
                    toast = Toast(requireContext())
                    toast?.setText(it.message)
                    toast?.duration = Toast.LENGTH_SHORT
                    toast?.show()
                }
            }
        })


    }

    private fun findViews(view: View) {
        recyclerView = view.findViewById(R.id.listRecyclerView)
        val manager = LinearLayoutManager(requireContext())
        manager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(BottomScrolledListener { loadMore() })


        swipeRefresh = view.findViewById(R.id.listSwipeRefresh)
        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
//            viewModel.refresh(checkConnection())
            viewModel.refreshDifferently(checkConnection())
        }

        swipeRefresh.setOnRefreshListener(refreshListener)
    }

    private fun loadMore(){
//        viewModel.loadMore(checkConnection())
        viewModel.loadMoreDifferently(checkConnection())
    }

    override fun onPause() {
        toast?.cancel()
        super.onPause()

    }


    companion object{
        const val LIST_TAG = "UsersList"
    }

}