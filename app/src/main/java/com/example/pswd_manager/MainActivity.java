package com.example.pswd_manager;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button addBtn;
    private Button resetPasswordBtn;
    private DatabaseHelper dbHelper;
    private LinearAdapter adapter;
    private List<List<String>> stringDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mRvMain = findViewById(R.id.re_view);

        // Initialize database
        dbHelper = new DatabaseHelper(this, "PasswordRecord.db", null, 1);
        dbHelper.getWritableDatabase();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("PasswordRecord",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                String website_name = cursor.getString(1);
                String username = cursor.getString(2);
                String password = cursor.getString(3);

                List<String> record = new ArrayList<>();
                record.add(website_name);
                record.add(username);
                record.add(password);
                stringDataList.add(record);
                cursor.moveToNext();
            }
        }
        cursor.close();

        adapter = new LinearAdapter(MainActivity.this, new LinearAdapter.OnItemClickListener() {
            // Click eye-button to display or hide password
            @Override
            public void onEyeBtnClick(LinearViewHolder holder, int pos) {
                // Hide password
                if(holder.viewPassword){
                    holder.eye_btn.setBackground(getResources().getDrawable(R.drawable.ic_eye_slash));
                    holder.viewPassword = false;

                    String password = stringDataList.get(pos).get(2);
                    int passwordLength = password.length();
                    StringBuilder hidePassword = new StringBuilder();
                    for(int i = 0; i<passwordLength; i++){
                        hidePassword.append("*");
                    }
                    holder.password.setText(hidePassword.toString());
                }
                // If viewPassword state = false, change to true
                else{
                    // Authenticate user before user can view password
                    LayoutInflater inflater = LayoutInflater.from(getApplication());
                    View authenticateView = inflater.inflate(R.layout.dialog_authenticate, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Authenticate User")
                            .setView(authenticateView)
                            .setPositiveButton("Confirm", null);
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    // Click confirm button to show password
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    EditText pinNumber = authenticateView.findViewById(R.id.etPinNumber);
                                    String pin = pinNumber.getText().toString();
                                    if(!pin.equals("")){
                                        SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
                                        String pinNum = sharedPreferences.getString("pinNumber", "");

                                        if(pin.equals(pinNum)){
                                            Toast.makeText(MainActivity.this, "Succeed authenticated", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            holder.eye_btn.setBackground(getResources().getDrawable(R.drawable.ic_eye_opened));
                                            holder.viewPassword = true;
                                            String password = stringDataList.get(pos).get(2);
                                            holder.password.setText(password);
                                        }
                                        else {
                                            Toast.makeText(MainActivity.this, "Failed authenticated", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "Empty input", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

            // Edit record
            @Override
            public void onEditBtnClick(LinearViewHolder holder, int pos) {
                LayoutInflater inflater = LayoutInflater.from(getApplication());
                View authenticateView = inflater.inflate(R.layout.dialog_authenticate, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Authenticate User")
                        .setView(authenticateView)
                        .setPositiveButton("Confirm", null);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Authenticate user before update record
                                EditText pinNumber = authenticateView.findViewById(R.id.etPinNumber);
                                String pin = pinNumber.getText().toString();

                                if(!pin.equals("")){
                                    SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
                                    String pinNum = sharedPreferences.getString("pinNumber", "");

                                    if(pin.equals(pinNum)){
                                        Toast.makeText(MainActivity.this, "Succeed authenticated", Toast.LENGTH_SHORT).show();
                                        LayoutInflater inflater = LayoutInflater.from(getApplication());
                                        View editView = inflater.inflate(R.layout.dialog_edit_record, null);
                                        // Website name
                                        String edit_website_name = stringDataList.get(pos).get(0);
                                        EditText eWebsite_name = editView.findViewById(R.id.etWebsiteName);
                                        eWebsite_name.setInputType(EditorInfo.TYPE_NULL);
                                        eWebsite_name.setText(edit_website_name);

                                        // Username
                                        String edit_username = stringDataList.get(pos).get(1);
                                        EditText eUsername = editView.findViewById(R.id.etUsername);
                                        eUsername.setText(edit_username);

                                        // Password
                                        String edit_password = stringDataList.get(pos).get(2);
                                        EditText ePassword = editView.findViewById(R.id.etPassword);
                                        ePassword.setText(edit_password);

                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                                .setTitle("Edit Password Record")
                                                .setView(editView)
                                                .setPositiveButton("Confirm", null);
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                        // Click confirm button to update record
                                        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                                .setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        String edit_username = eUsername.getText().toString();
                                                        String edit_password = ePassword.getText().toString();

                                                        // Update database
                                                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                                                        ContentValues values = new ContentValues();
                                                        values.put("username",edit_username);
                                                        values.put("password",edit_password);
                                                        String whereClause = "website_name=?";
                                                        String[] whereArgs = {edit_website_name};

                                                        db.update("PasswordRecord",values,whereClause,whereArgs);

                                                        // Update data list (adapter item)
                                                        editRecord(pos, edit_username, edit_password);

                                                        dialog.dismiss();
                                                    }
                                                });

                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "Failed authenticated", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismiss();
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Empty input", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            // Delete record
            @Override
            public void onDeleteBtnClick(LinearViewHolder holder, int pos) {
                // Update database
                String delete_website_name = holder.website_name.getText().toString();
                String delete_username = holder.username.getText().toString();

                String whereClause = "website_name=? and username=?";
                String[] whereArgs = {delete_website_name, delete_username};
                db.delete("PasswordRecord",whereClause,whereArgs);

                // Update data list (adapter item)
                deleteRecord(pos);
            }
        });
        mRvMain.setAdapter(adapter);
        mRvMain.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        // Add record
        addBtn = findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getApplication());
                View addView = inflater.inflate(R.layout.dialog_add_record, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add Password Record")
                        .setView(addView)
                        .setPositiveButton("Confirm", null);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                // Click confirm button to add record
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View view) {
                        EditText aWebsite_name = addView.findViewById(R.id.etWebsiteName);
                        EditText aUsername = addView.findViewById(R.id.etUsername);
                        EditText aPassword = addView.findViewById(R.id.etPassword);
                        String add_website_name = aWebsite_name.getText().toString();
                        String add_username = aUsername.getText().toString();
                        String add_password = aPassword.getText().toString();

                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        // Input validation: Duplicate record
                        String[] params = {add_website_name, add_username};
                        Cursor cursor = db.query("PasswordRecord",null,"website_name=? and username=?",params,null,null,null);
                        if (cursor.getCount()>0){
                            Toast.makeText(MainActivity.this, "Failed insert: Record existed", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            // Update database
                            ContentValues values = new ContentValues();
                            values.put("website_name",add_website_name);
                            values.put("username",add_username);
                            values.put("password",add_password);
                            db.insert("PasswordRecord",null,values);

                            // Update data list (adapter item)
                            List<String> record = new ArrayList<>();
                            record.add(add_website_name);
                            record.add(add_username);
                            record.add(add_password);
                            adapter.getStringDataList().add(record);
                            adapter.notifyDataSetChanged();

                            stringDataList.add(record);

                            Toast.makeText(MainActivity.this, "New Record Added", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                //dialog.getWindow().setLayout(1040,1020);
            }
        });

        // Reset authenticate password
        resetPasswordBtn = findViewById(R.id.reset_password_btn);
        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getApplication());
                View authenticateView = inflater.inflate(R.layout.dialog_safety_authentication, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Authenticate User")
                        .setView(authenticateView)
                        .setPositiveButton("Confirm", null);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EditText birthdayMonth = (EditText) authenticateView.findViewById(R.id.birthdayMonth);
                                String birthdayMonth_txt = birthdayMonth.getText().toString();
                                EditText fatherBirthdayMonth = (EditText) authenticateView.findViewById(R.id.fatherBirthdayMonth);
                                String fatherBirthdayMonth_txt = fatherBirthdayMonth.getText().toString();
                                EditText motherBirthdayMonth = (EditText) authenticateView.findViewById(R.id.motherBirthdayMonth);
                                String motherBirthdayMonth_txt = motherBirthdayMonth.getText().toString();
                                EditText favouriteColor = (EditText) authenticateView.findViewById(R.id.favouriteColor);
                                String favouriteColor_txt = favouriteColor.getText().toString();

                                if (birthdayMonth_txt.equals("") || fatherBirthdayMonth_txt.equals("") || motherBirthdayMonth_txt.equals("") || favouriteColor_txt.equals("")) {
                                    Toast.makeText(MainActivity.this, "Empty input", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
                                    String birthdayMonth_saved = sharedPreferences.getString("birthdayMonth", "");
                                    String fatherBirthdayMonth_saved = sharedPreferences.getString("fatherBirthdayMonth", "");
                                    String motherBirthdayMonth_saved = sharedPreferences.getString("motherBirthdayMonth", "");
                                    String favouriteColor_saved = sharedPreferences.getString("favouriteColor", "");

                                    if(birthdayMonth_txt.equals(birthdayMonth_saved) && fatherBirthdayMonth_txt.equals(fatherBirthdayMonth_saved) && motherBirthdayMonth_txt.equals(motherBirthdayMonth_saved) && favouriteColor_txt.equals(favouriteColor_saved)){
                                        Toast.makeText(MainActivity.this, "Succeed authenticated", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, SetAuthenticatePasswordActivity.class);
                                        intent.putExtra("title","resetAuthentication");
                                        startActivity(intent);
                                        MainActivity.this.finish();
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "Failed authenticated", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }
                            }
                        });
            }
        });
    }

    // Edit record
    public void editRecord(int pos, String username, String password){
        List<List<String>> adap_stringDataList = adapter.getStringDataList();
        System.out.println(pos);

        adap_stringDataList.get(pos).set(1,username);
        adap_stringDataList.get(pos).set(2,password);
        this.stringDataList.get(pos).set(1,username);
        this.stringDataList.get(pos).set(2,password);

        adapter.notifyDataSetChanged();
    }

    // Delete record
    public void deleteRecord(int pos){
        adapter.getStringDataList().remove(pos);
        this.stringDataList.remove(pos);
        adapter.notifyDataSetChanged();
    }
}

// View holder
class LinearViewHolder extends RecyclerView.ViewHolder{
    TextView website_name;
    TextView username;
    TextView password;
    Button eye_btn;
    Button edit_btn;
    Button delete_btn;
    boolean viewPassword = false;

    public LinearViewHolder(@NonNull View itemView){
        super(itemView);
        website_name = itemView.findViewById(R.id.website_name);
        username = itemView.findViewById(R.id.username);
        password = itemView.findViewById(R.id.password);
        eye_btn = itemView.findViewById(R.id.eye_btn);
        edit_btn = itemView.findViewById(R.id.edit_btn);
        delete_btn = itemView.findViewById(R.id.delete_btn);
    }
}

// Adapter
class LinearAdapter extends RecyclerView.Adapter<LinearViewHolder> {
    private List<List<String>> stringDataList = new ArrayList<>(); // Data list to store all record
    private Context mContext;
    private OnItemClickListener mListener;

    public LinearAdapter(Context context, OnItemClickListener listener){
        this.mContext = context;
        this.mListener = listener;

        // Retrieve data from database
        DatabaseHelper dbHelper;
        dbHelper = new DatabaseHelper(context, "PasswordRecord.db", null, 1);
        dbHelper.getWritableDatabase();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("PasswordRecord",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                String website_name = cursor.getString(1);
                String username = cursor.getString(2);
                String password = cursor.getString(3);

                List<String> record = new ArrayList<>();
                record.add(website_name);
                record.add(username);
                record.add(password);
                stringDataList.add(record);
                cursor.moveToNext();
            }
        }
        else {
            System.out.println("Empty");
        }
        cursor.close();
    }

    @NonNull
    @Override
    public LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pswd, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, int position) {
        String website_name = stringDataList.get(position).get(0);
        String username = stringDataList.get(position).get(1);
        String password = stringDataList.get(position).get(2);

        holder.website_name.setText(website_name);
        holder.username.setText(username);
        holder.password.setText(password);
        int passwordLength = password.length();
        StringBuilder hidePassword = new StringBuilder();
        for(int j = 0; j<passwordLength; j++){
            hidePassword.append("*");
        }
        holder.password.setText(hidePassword.toString());
        holder.eye_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onEyeBtnClick(holder, position);
            }
        });
        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onEditBtnClick(holder, position);
            }
        });
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDeleteBtnClick(holder, position);
            }
        });
    }

    public List<List<String>> getStringDataList() {
        return stringDataList;
    }

    public interface OnItemClickListener{
        void onEyeBtnClick(LinearViewHolder holder, int pos); // Click eye-button to display or hide password
        void onEditBtnClick(LinearViewHolder holder, int pos); // Click edit-button to display or hide password
        void onDeleteBtnClick(LinearViewHolder holder, int pos); // Click bin-button to display or hide password
    }

    @Override
    public int getItemCount() {
        return stringDataList.size();
    }

}