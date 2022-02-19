package com.mahdi.d.o.taha.aym.adapters

import com.mahdi.d.o.taha.aym.R
import com.mahdi.d.o.taha.aym.databinding.MsgItemBinding
import com.mahdi.d.o.taha.aym.models.Msg_model
import com.xwray.groupie.databinding.BindableItem

class ReceiveMessageItem(private val message: Msg_model) : BindableItem<MsgItemBinding>() {
    override fun bind(viewBinding: MsgItemBinding, position: Int) {
        viewBinding.msg = message
    }

    override fun getLayout(): Int {
        return R.layout.msg_item
    }
}
