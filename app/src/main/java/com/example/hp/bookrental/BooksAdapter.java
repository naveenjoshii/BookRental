package com.example.hp.bookrental;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

/**
 * Created by HP on 16-06-2018.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {
    private Context context;
    private List<BooksDetails> mBooks;

    public BooksAdapter(Context context, List<BooksDetails> mBooks) {
        this.context = context;
        this.mBooks = mBooks;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.books_item,parent,false);
        return new BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position){
        BooksDetails booksDetails = mBooks.get(position);
        holder.bookViewName.setText(booksDetails.getmBookName());
        Picasso.with(context)
                .load(booksDetails.getmImageurl())
                .fit()
                .centerCrop()
                .into(holder.imageView);
        //holder.bookAuthor.setText(booksDetails.getmAuthorName());
        //holder.bookEdition.setText(booksDetails.getmEditionNo());
        //holder.bookStatus.setText(booksDetails.getmStatus());
        holder.bookPrice.setText(booksDetails.getmPrice());
        holder.bookDate.setText(booksDetails.getmDate());
                holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                BooksDetails bd = mBooks.get(pos);
                Intent i = new Intent(context,BookInfo.class);
                i.putExtra("Book image",bd.getmImageurl());
                i.putExtra("Book Name",bd.getmBookName());
                i.putExtra("Book Author",bd.getmAuthorName());
                i.putExtra("Book Edition",bd.getmEditionNo());
                i.putExtra("Book Price",bd.getmPrice());
                i.putExtra("Book Status",bd.getmStatus());
                i.putExtra("Book userid",bd.getmUserId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public  class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView bookViewName, bookDate, bookPrice;
        public ImageView imageView;
        private ItemClickListener itemClickListener;

        public BookViewHolder(View itemView) {
            super(itemView);
            bookDate = itemView.findViewById(R.id.item_time);
            bookViewName = itemView.findViewById(R.id.item_bName);
            imageView = itemView.findViewById(R.id.item_image);
            bookPrice = itemView.findViewById(R.id.item_bPrice);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener ic)
        {
            this.itemClickListener=ic;

        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }
    }


}
