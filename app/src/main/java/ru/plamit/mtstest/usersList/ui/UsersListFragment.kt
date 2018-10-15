package ru.plamit.mtstest.usersList.ui

import android.arch.lifecycle.Observer
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
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
        private const val BASE_USER = "octocat"

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
        usersList.setHasFixedSize(true)
        usersList.layoutManager = getLayoutManager()
        usersList.adapter = adapter
        loadingPb.visible()
        usersViewModel.viewState.observe(this, Observer { usersItems ->
            loadingPb.gone()
            activity?.title = usersViewModel.selectedUser
            if (usersItems?.isEmpty() == true) emptyList.visible()
            adapter.items = ArrayList(usersItems)
        })
        usersViewModel.router = this
        if (savedInstanceState == null) usersViewModel.selectUser(BASE_USER)
    }

    override fun routeToError(message: String) {
        val errorDialog = ErrorMessageDialogFragment.buildDialog(getString(R.string.error), message, click = {
            usersViewModel.selectUser(usersViewModel.selectedUser)
            it.dismiss()
        })
        errorDialog.show(activity?.supportFragmentManager, "error")
    }

    override fun onItemSelected(login: String) {
        usersViewModel.selectUser(login)
        loadingPb.visible()
    }

    /**
     * check for current user and select base user on back key pressed
     * @return true if current user is base user
     */
    fun onBackPressed(): Boolean {
        if (usersViewModel.selectedUser != BASE_USER) {
            usersViewModel.selectUser(BASE_USER)
            return false
        }
        return true
    }

    private fun getLayoutManager() =
            StaggeredGridLayoutManager(
                    if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 1 else 2,
                    StaggeredGridLayoutManager.VERTICAL)

}
