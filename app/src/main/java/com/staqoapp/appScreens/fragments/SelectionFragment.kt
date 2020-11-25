package com.staqoapp.appScreens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.staqoapp.appScreens.MainViewModel
import com.staqoapp.appScreens.fragments.SelectionFragmentDirections
import com.google.android.material.snackbar.Snackbar
import com.staqoapp.R
import com.staqoapp.databinding.FragmentSelectionBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SelectionFragment : Fragment() {
    private lateinit var mBinding: FragmentSelectionBinding
    private val viewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_selection, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()
    }

    private fun onClicks() {
        mBinding.cardViewVerticalList.setOnClickListener {
            if(viewModel.userList.isNotEmpty())
                findNavController().navigate(SelectionFragmentDirections.actionSelectionFragmentToVerticalListFragment())
            else {
                Snackbar.make(
                    mBinding.textViewHorizontalList,
                    getString(R.string.no_user_list_found),
                    Snackbar.LENGTH_SHORT
                ).show()

                viewModel.getUsers()
            }
        }

        mBinding.cardViewHorizontalList.setOnClickListener {
            if(viewModel.userList.isNotEmpty())
                findNavController().navigate(SelectionFragmentDirections.actionSelectionFragmentToHorizontalListFragment())
            else {
                Snackbar.make(
                    mBinding.textViewHorizontalList,
                    getString(R.string.no_user_list_found),
                    Snackbar.LENGTH_SHORT
                ).show()

                viewModel.getUsers()
            }
        }
    }
}