import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.surround2023.surround2023.community.Communitymemo
import com.surround2023.surround2023.databinding.ItemCommunitymemoBinding

class Communityadapter : RecyclerView.Adapter<Communityadapter.PostViewHolder>() {

    private var postList: List<Communitymemo> = emptyList()

    fun setData(posts: List<Communitymemo>) {
        postList = posts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemCommunitymemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(postList[position])
    }

    override fun getItemCount(): Int = postList.size

    class PostViewHolder(private val binding: ItemCommunitymemoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Communitymemo) {
            binding.ivImage.setImageResource(post.image)
            binding.tvCategory.text = post.category
            binding.tvTitle.text = post.postTitle
            binding.tvDetail.text = post.postContent
            binding.tvComment.text = post.comment
            // 게시글의 추가 정보를 표시하고자 한다면 여기서 처리합니다.
        }
    }
}