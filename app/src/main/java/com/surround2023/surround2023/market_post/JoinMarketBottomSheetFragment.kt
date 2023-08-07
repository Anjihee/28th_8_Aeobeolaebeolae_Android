package com.surround2023.surround2023.market_post

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.surround2023.surround2023.R
import java.text.NumberFormat

class JoinMarketBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var checkedJoinWay: String

    private var priceData: String? = null
    private var postId: String? = null

    companion object {
        const val ARG_DATA1 = "priceData"
        const val ARG_DATA2 = "postId"

        fun newInstance(priceData: String, postId: String): JoinMarketBottomSheetFragment {
            val fragment = JoinMarketBottomSheetFragment()
            val args = Bundle()
            args.putString(ARG_DATA1, priceData)
            args.putString(ARG_DATA2, postId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottomsheet_join_market, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // BottomSheet 레이아웃과 상호작용하는 코드를 추가할 수 있습니다.

        // 가격 데이터 받아옴
        priceData = arguments?.getString(ARG_DATA1)
        // postId 데이터 받아옴
        postId = arguments?.getString(ARG_DATA2)

        val payment = view.findViewById<TextView>(R.id.payment)
        payment.text = priceData

        val directJoinBtn = view.findViewById<RadioButton>(R.id.directJoinBtn)
        val deadlineJoinBtn = view.findViewById<RadioButton>(R.id.deadlineJoinBtn)
        val finalJoinBtn = view.findViewById<Button>(R.id.finalJoinbtn)

        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.directJoinBtn) {
                // '즉시 참여' 버튼이 선택된 경우
                directJoinBtn.setTextColor(resources.getColor(R.color.themeGreen)) // 선택된 버튼
                deadlineJoinBtn.setTextColor(resources.getColor(R.color.colorGrey))
                checkedJoinWay = "직거래"
            } else if (checkedId == R.id.deadlineJoinBtn) {
                // '마감일 참여' 버튼이 선택된 경우
                directJoinBtn.setTextColor(resources.getColor(R.color.colorGrey)) 
                deadlineJoinBtn.setTextColor(resources.getColor(R.color.themeGreen)) // 선택된 버튼
                checkedJoinWay = "택배거래"
            } else { // 어떤 버튼도 선택되지 않음
                directJoinBtn.setTextColor(resources.getColor(R.color.colorGrey))
                deadlineJoinBtn.setTextColor(resources.getColor(R.color.colorGrey))
                checkedJoinWay = "수령 안함"
            }
        }

        finalJoinBtn.setOnClickListener { // '공동구매 참여하기' 버튼 클릭 시
            // 컨텍스트 가져오기
            val context = requireContext()

            // 눌러짐 색상 변경
            val colorStateList = ContextCompat.getColorStateList(context, R.color.subDeepGreen)
            finalJoinBtn.backgroundTintList = colorStateList

            // 200ms(0.2초) 후에 원래 색상으로 복원
            finalJoinBtn.postDelayed({
                val originalColorStateList = ContextCompat.getColorStateList(context, R.color.themeGreen)
                finalJoinBtn.backgroundTintList = originalColorStateList
            }, 200)

            // '공동구매 참여하기' 버튼을 클릭하면 참여 상세 페이지를 열도록 인텐트 생성
            val intent = Intent(context, JoinMarketActivity::class.java)
            if (checkedJoinWay == "수령 안함") {
                Toast.makeText(context, "수령 방법을 선택해 주세요.", Toast.LENGTH_SHORT).show()
            } else {
                intent.putExtra("joinWay", checkedJoinWay)
                intent.putExtra("postId", postId)
                startActivity(intent)
                dismiss()
            }
        }
    }
}
