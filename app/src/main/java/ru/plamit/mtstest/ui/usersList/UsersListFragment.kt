package ru.plamit.mtstest.ui.usersList

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ru.plamit.mtstest.R

class UsersListFragment : Fragment() {

    private var mViewModel: UsersListViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(UsersListViewModel::class.java)
    }

    companion object {

        fun newInstance(): UsersListFragment {
            return UsersListFragment()
        }
    }

}
