package ru.plamit.mtstest.usersList.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.users_list_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.plamit.mtstest.R
import ru.plamit.mtstest.usersList.IUsersListRouter
import ru.plamit.mtstest.utils.ErrorMessageDialogFragment
import ru.plamit.mtstest.utils.gone
import ru.plamit.mtstest.utils.visible

class UsersListFragment : Fragment(), IUsersListRouter, UsersListAdapter.ItemSelectionListener {

    private val usersViewModel: UsersListViewModel by viewModel()

    private val adapter = UsersListAdapter()

    companion object {
        fun newInstance(): UsersListFragment {
            return UsersListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.users_list_fragment, container, false)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.itemSelectionListener = this
        usersList.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        usersList.adapter = adapter
        loadingPb.visible()
        usersViewModel.viewState.observe(this, Observer { usersItems ->
            loadingPb.gone()
            if (usersItems?.isEmpty() == true) emptyList.visible()
            adapter.items = ArrayList(usersItems)
        })
    }

    override fun onResume() {
        super.onResume()
        usersViewModel.router = this
        usersViewModel.selectUser("octocat")
    }

    override fun routeToError(message: String) {
        val errorDialog = ErrorMessageDialogFragment.buildDialog(getString(R.string.error),message,click = {
            usersViewModel.selectUser("octocat")
        })
        errorDialog.show(activity?.supportFragmentManager,"error")
    }

    override fun onItemSelected(login: String) {
        usersViewModel.selectUser(login)
        loadingPb.visible()
    }
}
