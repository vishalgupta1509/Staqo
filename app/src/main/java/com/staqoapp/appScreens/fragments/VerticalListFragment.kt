package com.staqoapp.appScreens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.staqoapp.R
import com.staqoapp.appScreens.MainViewModel
import com.staqoapp.databinding.VerticalListFragmentBinding
import com.staqoapp.utils.Constants.OtherConstants.VERTICAL
import com.staqoapp.appScreens.fragments.UserListAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class VerticalListFragment : Fragment() {
    private lateinit var mBinding: VerticalListFragmentBinding
    private val viewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.vertical_list_fragment, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListAdapter()
    }

    private fun setListAdapter() {
        viewModel.userMutableData.observe(viewLifecycleOwner, {
            if(it.isNotEmpty()) {
                mBinding.recyclerViewVertical.adapter = UserListAdapter(VERTICAL, requireContext(), it)
            } else {
                viewModel.getUsers()
            }
        })
    }
}