package com.mahdi.d.o.taha.aym.adapters

import com.mahdi.d.o.taha.aym.R
import com.mahdi.d.o.taha.aym.databinding.MsgItemSendBinding
import com.mahdi.d.o.taha.aym.models.Msg_model
import com.xwray.groupie.databinding.BindableItem

class SendMessageItem(private val message: Msg_model) : BindableItem<MsgItemSendBinding>() {
    override fun bind(viewBinding: MsgItemSendBinding, position: Int) {
        viewBinding.msg = message
    }

    override fun getLayout(): Int {
        return R.layout.msg_item_send
    }
}
