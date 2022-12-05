package hr.algebra.personmanager

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.personmanager.dao.Person
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class PersonAdapter(private val context:Context, private val people: MutableList<Person>,
                    private val navigableFragment: NavigableFragment)
    : RecyclerView.Adapter<PersonAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivImage = itemView.findViewById<ImageView>(R.id.ivImage)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val ivDelete = itemView.findViewById<ImageView>(R.id.ivDelete)
        fun bind(person: Person) {
            tvTitle.text = person.toString()
            Picasso.get()
                .load(File(person.picturePath))
                .error(R.mipmap.ic_launcher)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivImage)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(itemView = LayoutInflater.from(context).inflate(R.layout.list_person, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ivDelete.setOnClickListener {


            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    (context?.applicationContext as App).getPersonDao().delete(people[position])
                }
                people.removeAt(position)
                notifyDataSetChanged()
            }
        }
        holder.itemView.setOnLongClickListener {
            navigableFragment.navigate(Bundle().apply {
                putLong(PERSON_ID, people[position]._id!!)
            })
            true
        }
        holder.bind(people[position])
    }

    override fun getItemCount() = people.size
}