package com.staqoapp.appScreens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.staqoapp.appScreens.MainViewModel
import com.staqoapp.utils.Constants.OtherConstants.HORIZONTAL
import com.google.android.material.tabs.TabLayoutMediator
import com.staqoapp.R
import com.staqoapp.databinding.FragmentHorizontalListBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class HorizontalListFragment : Fragment() {
    private lateinit var mBinding: FragmentHorizontalListBinding
    private val viewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_horizontal_list, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListAdapter()
    }

    private fun setListAdapter() {
        viewModel.userMutableData.observe(viewLifecycleOwner, {
            if(it.isNotEmpty()) {
                mBinding.viewPager.adapter = UserListAdapter(HORIZONTAL, requireContext(), it)

                TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager) { tab, position ->
                    //Some implementation
                }.attach()
            } else {
                viewModel.getUsers()
            }
        })
    }
}