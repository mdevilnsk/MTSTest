package ru.plamit.mtstest.usersList.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.plamit.mtstest.R
import ru.plamit.mtstest.usersList.IUsersListRouter
import ru.plamit.mtstest.utils.ErrorMessageDialogFragment

class UsersListFragment : Fragment(), IUsersListRouter {

    private val usersViewModel: UsersListViewModel by viewModel()

    companion object {
        fun newInstance(): UsersListFragment {
            return UsersListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usersViewModel.viewState.observe(this, Observer { usersItems ->
            var users = ""
            usersItems?.forEach{
                users += "login: ${it.login}, ava: ${it.avatar}, name: ${it.name}, email: ${it.email}\n"
            }
            message.text = users
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
}
