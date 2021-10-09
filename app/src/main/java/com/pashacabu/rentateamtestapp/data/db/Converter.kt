package com.pashacabu.rentateamtestapp.data.db

import com.pashacabu.rentateamtestapp.data.network.DataItem

class Converter {

    private fun dbItemToNetworkResp(item: Users): DataItem {
        val out = DataItem()
        out.id = item.id
        out.firstName = item.firstName
        out.lastName = item.lastName
        out.email = item.email
        out.avatar = item.avatar

        return out
    }

    private fun networkRespToDBItem(item: DataItem): Users {
        val out = Users()
        out.id = item.id
        out.firstName = item.firstName
        out.lastName = item.lastName
        out.email = item.email
        out.avatar = item.avatar

        return out
    }

    fun dbListToNRList(list: List<Users>): MutableList<DataItem?> {
        val out = mutableListOf<DataItem?>()
        for (user in list) {
            out.add(dbItemToNetworkResp(user))
        }
        return out
    }

    fun nrListToDBList(list: MutableList<DataItem?>?): List<Users> {
        val out = mutableListOf<Users>()
        if (list != null) {
            for (user in list) {
                user?.let { networkRespToDBItem(it) }?.let { out.add(it) }
            }
        }
        return out
    }

}