import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.surround2023.surround2023.community.Communitymemo
import com.surround2023.surround2023.databinding.ActivityCommunityholderBinding

class CommunityholderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityholderBinding
    private lateinit var adapter: Communityadapter
    private val db = FirebaseFirestore.getInstance()
    private val postRef = db.collection("Community").document("Post01")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityholderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView 초기화
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = Communityadapter()
        binding.recyclerview.adapter = adapter

        // Firestore에서 데이터 가져오기
        fetchPostFromFirestore()
    }

    private fun fetchPostFromFirestore() {
        postRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val image = documentSnapshot.getLong("image")?.toInt() ?: 0
                    val category = documentSnapshot.getString("category") ?: ""
                    val title = documentSnapshot.getString("title") ?: ""
                    val detail = documentSnapshot.getString("detail") ?: ""
                    val comment = documentSnapshot.getString("comment") ?: ""

                    val post = Communitymemo(image, category, title, detail, comment)
                    adapter.setData(listOf(post))
                }
            }
            .addOnFailureListener { exception ->
                // 에러 처리
                // 예: Toast.makeText(this, "데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
    }
}