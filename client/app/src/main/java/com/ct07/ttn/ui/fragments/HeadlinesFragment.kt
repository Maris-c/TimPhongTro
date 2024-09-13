package com.ct07.ttn.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ct07.ttn.R
import com.ct07.ttn.adapters.RoomAdapter
import com.ct07.ttn.databinding.FragmentHeadlinesBinding
import com.ct07.ttn.models.User
import com.ct07.ttn.ui.RoomActivity
import com.ct07.ttn.ui.room.RoomViewModel
import com.ct07.ttn.ui.user.UserViewModel
import com.ct07.ttn.util.Constants
import com.ct07.ttn.util.Resource

class HeadlinesFragment : Fragment(R.layout.fragment_headlines) {

    lateinit var roomViewModel: RoomViewModel
    lateinit var userViewModel: UserViewModel
    lateinit var roomAdapter: RoomAdapter
    lateinit var retryButton: Button
    lateinit var errorText: TextView
    lateinit var itemHeadlinesError: CardView
    lateinit var binding: FragmentHeadlinesBinding

    var user: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHeadlinesBinding.bind(view)

        itemHeadlinesError = view.findViewById(R.id.itemHeadlinesError)

        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_error, null)

        retryButton = view.findViewById(R.id.retryButton)
        errorText = view.findViewById(R.id.errorText)

        roomViewModel = (activity as RoomActivity).roomViewModel
        userViewModel = (activity as RoomActivity).userViewModel
        user = userViewModel.getUser()
        user?.let {
            roomViewModel.getFavorites(it.id)
        }
        setupHeadlinesRecycler()

        roomAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("room", it)
            }
            findNavController().navigate(R.id.action_headlinesFragment_to_roomFragment, bundle)
        }

        roomViewModel.headlines.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success<*> -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let {roomResponse ->
                        roomAdapter.differ.submitList(roomResponse.toList())
                        val totalPages = 10 / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = roomViewModel.headlinesPage == totalPages
                        if (isLastPage){
                            binding.recyclerHeadlines.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error<*> -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "Đã có lỗi xảy ra: $message", Toast.LENGTH_LONG).show()
                        showErrorMessage(message)
                    }
                }
                is Resource.Loading<*> -> {
                    showProgressBar()
                }
            }
        })

        retryButton.setOnClickListener {
            roomViewModel.getHeadlines()
        }

        binding.btnSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // chưa có xử lý
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.isEmpty()) {
                    roomViewModel.getHeadlines()
                } else {
                    roomViewModel.searchRoom(query)
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Chưa có xử lý
            }
        })

/*        binding.swipeRefreshLayout.setOnRefreshListener {
            roomViewModel.getHeadlines()
        }*/

    }
    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage(){
        itemHeadlinesError.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String){
        itemHeadlinesError.visibility = View.VISIBLE
        errorText.text = message
        isError = true
    }

    val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                roomViewModel.getHeadlines()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

        private fun setupHeadlinesRecycler(){
            roomAdapter = RoomAdapter(roomViewModel, userViewModel, viewLifecycleOwner)
            binding.recyclerHeadlines.apply {
                adapter = roomAdapter
                layoutManager = LinearLayoutManager(activity)
                addOnScrollListener(this@HeadlinesFragment.scrollListener)
        }
    }
}