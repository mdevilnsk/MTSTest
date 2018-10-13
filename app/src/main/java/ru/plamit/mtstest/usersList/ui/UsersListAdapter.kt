package ru.plamit.mtstest.usersList.ui

import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.user_item.view.*
import org.koin.standalone.KoinComponent
import ru.plamit.mtstest.R
import ru.plamit.mtstest.utils.onClick

class UsersListAdapter : RecyclerView.Adapter<UsersListAdapter.CurrencyViewHolder>(), KoinComponent {

    var itemSelectionListener: ItemSelectionListener? = null

    var items: MutableList<UserViewItem> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface ItemSelectionListener{
        fun onItemSelected(login: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int) =
            CurrencyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(viwHolder: CurrencyViewHolder, position: Int) {
        val item = items[position]
        viwHolder.itemView.onClick {
            itemSelectionListener?.onItemSelected(item.login)
        }
        viwHolder.itemView.apply {
            //picasso userAva
            userLogin.text = item.login
            userName.text = item.name
            userWork.text = item.companyName
            userEmail.text = item.email
        }
    }

    class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}