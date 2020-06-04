package com.ylh.huqidiary.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import com.ylh.huqidiary.R;
import com.ylh.huqidiary.bean.DiaryBean;
import com.ylh.huqidiary.bean.TextTypeBean;
import com.ylh.huqidiary.db.DiaryDatabaseHelper;
import com.ylh.huqidiary.event.MessageEvent;
import com.ylh.huqidiary.utils.*;
import com.ylh.huqidiary.widget.ColorPickerView;
import com.ylh.huqidiary.widget.LinedEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.trity.floatingactionbutton.FloatingActionButton;
import cc.trity.floatingactionbutton.FloatingActionsMenu;
import com.ylh.huqidiary.widget.MyDialog;
import com.ylh.huqidiary.widget.PhotoPopupWindow;
import org.greenrobot.eventbus.EventBus;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddDiaryActivity extends Activity {

    @Bind(R.id.add_diary_tv_date)
    TextView mAddDiaryTvDate;
    @Bind(R.id.add_diary_tv_time)
    TextView mAddDiaryTvTime;
    @Bind(R.id.add_diary_tv_week)
    TextView mAddDiaryTvWeek;
    @Bind(R.id.add_diary_tv_weather)
    TextView mAddDiaryTvWeather;

    @Bind(R.id.add_diary_et_content)
    LinedEditText mAddDiaryEtContent;
    @Bind(R.id.add_diary_fab_back)
    FloatingActionButton mAddDiaryFabBack;
    @Bind(R.id.add_diary_fab_add)
    FloatingActionButton mAddDiaryFabAdd;
    @Bind(R.id.right_labels)
    FloatingActionsMenu mRightLabels;

    @Bind(R.id.common_iv_back)
    ImageView mCommonIvBack;
    @Bind(R.id.common_iv_search)
    ImageView mCommonIvSearch;
    @Bind(R.id.common_iv_girl)
    ImageView mCommonIvGirl;
    @Bind({R.id.common_tv_complete})
    TextView mCommonTvComplete;
    @Bind(R.id.seekbar_aa)
    AppCompatSeekBar mSeekBar;
    @Bind(R.id.color_pick)
    ColorPickerView mColorPick;
    @Bind(R.id.ll_addDiary_page)
    LinearLayout mAddDiaryPage;

    private static final String TAG = "AddDiaryActivity";
    private static final String ADDDIARY_BACKGROUND = "addDiary_background";
    private static final String BITMAP = "bitmap";
    private static final String TEXT_TYPE = "text_type";
    private static final int REQUEST_IMAGE_GET = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_IMAGE_GET_BACKGROUND = 3;

    private DiaryDatabaseHelper mHelper;
    private PhotoPopupWindow mPhotoPopupWindow;

    //在OnActivityResult回调的时候发现intetn的data值为null。如果企图通过data取值就会崩溃，
    //查看相关资料，Android调用相机相关源码：
    //得出在手动指定了uri之后，data就会为空。
    //所以指定uri，将图片路径定义全局，不使用data获取先关数据
    private Uri imageUriFromCamera;
    private MyDialog.Builder dialog = null;
    private Bundle bundle = null;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddDiaryActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, AddDiaryActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防止软键盘把布局顶开
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_add_diary);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initView();
        mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
        if (ContextCompat.checkSelfPermission(AddDiaryActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 权限还没有授予，进行申请
            ActivityCompat.requestPermissions(AddDiaryActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200); // 申请的 requestCode 为 200
        }
    }

    private void initView() {
        mCommonIvSearch.setVisibility(View.GONE);
        mCommonIvGirl.setVisibility(View.GONE);
        Intent intent = getIntent();
        bundle = intent.getExtras();
        if (bundle != null) {
            DiaryBean mBean = (DiaryBean) bundle.getSerializable("diaryObj");
            mAddDiaryTvDate.setText(mBean.getDate());
            // content
            String imageAndText = mBean.getContent();
            SpannableString ss = new SpannableString(imageAndText);
            String regex = "\\[local]" + "(.*?)" + "\\[/local]";
            Pattern pattern = Pattern.compile(regex);
            Matcher m = pattern.matcher(imageAndText);
            while(m.find()) {
                String path = m.group(1);
                Log.e(TAG, path);
                Bitmap bm = null;
                bm = BitmapFactory.decodeFile(path);
                if (bm != null){
                    int bwidth = bm.getWidth();
                    int bHeight = bm.getHeight();
                    int width = ScreenUtils.getScreenWidth(AddDiaryActivity.this);
                    int height = width * bHeight / bwidth;
                    Bitmap rbm = ImageUtils.resizeImage(bm, width, height);
                    ImageSpan span = new ImageSpan(this, rbm);
                    ss.setSpan(span, m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            mAddDiaryEtContent.setText(ss);

            mAddDiaryTvTime.setText(mBean.getTime());
            mAddDiaryTvWeek.setText(mBean.getWeek());
            mAddDiaryTvWeather.setText(mBean.getWeather());
        } else {
            mAddDiaryTvDate.setText(GetDate.getDate());
            mAddDiaryTvTime.setText(GetDate.getTime());
            mAddDiaryTvWeek.setText(GetDate.getWeek());
//        mAddDiaryTvWeather.setText(GetDate.getWeek());
        }
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mAddDiaryEtContent.setTextSize((float) (i + 12));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mColorPick.setIndicatorColor(Color.GREEN);
        mColorPick.setOrientation(ColorPickerView.Orientation.HORIZONTAL);
        mColorPick.setColors(Color.RED,Color.YELLOW,Color.GREEN, Color.BLUE, Color.WHITE,Color.DKGRAY,Color.MAGENTA);
        mColorPick.setOnColorPickerChangeListener(new ColorPickerView.OnColorPickerChangeListener() {
            @Override
            public void onColorChanged(ColorPickerView picker, int color) {
                mAddDiaryEtContent.setTextColor(color);
                mAddDiaryTvDate.setTextColor(color);
                mAddDiaryTvTime.setTextColor(color);
                mAddDiaryTvWeek.setTextColor(color);
                mAddDiaryTvWeather.setTextColor(color);
            }

            @Override
            public void onStartTrackingTouch(ColorPickerView picker) {

            }

            @Override
            public void onStopTrackingTouch(ColorPickerView picker) {

            }
        });
        // 保存的偏好设置
        Bitmap bm = SharedPreferencesUtil.getBitmapFromSp(this,ADDDIARY_BACKGROUND, BITMAP);
        if (bm != null){
            BitmapDrawable bd = new BitmapDrawable(this.getResources(), bm);
            bd.setTileModeXY(Shader.TileMode.REPEAT , Shader.TileMode.REPEAT );
            mAddDiaryPage.setBackground(bd);
        }
        TextTypeBean textTypeBean = (TextTypeBean) SharedPreferencesUtil.getObjFromSp(this,ADDDIARY_BACKGROUND,TEXT_TYPE);
        if (textTypeBean != null){
            int color = textTypeBean.getText_color();
            float size = textTypeBean.getText_size();
            mAddDiaryEtContent.setTextColor(color);
            mAddDiaryTvDate.setTextColor(color);
            mAddDiaryTvTime.setTextColor(color);
            mAddDiaryTvWeek.setTextColor(color);
            mAddDiaryTvWeather.setTextColor(color);
            mAddDiaryEtContent.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        }
    }

    @OnClick({R.id.common_iv_back, R.id.common_tv_complete,
            R.id.add_diary_et_content,
            R.id.add_diary_ib_aa, R.id.add_diary_ib_pifu, R.id.add_diary_ib_pic, R.id.add_diary_ib_weather,})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_iv_back:
                final String contentBack = mAddDiaryEtContent.getText().toString();
                if (!contentBack.trim().isEmpty()) {
                    // 自动保存
                    saveEditContent(contentBack);
                    MainActivity.startActivity(AddDiaryActivity.this);
                } else {
                    MainActivity.startActivity(this);
                }
                break;
            case R.id.add_diary_et_content:
                mSeekBar.setVisibility(View.INVISIBLE);
                mColorPick.setVisibility(View.INVISIBLE);
                break;
            case R.id.common_tv_complete:
                String content = mAddDiaryEtContent.getText().toString();
                if (!content.trim().equals("")){
                    saveEditContent(content);
                    MainActivity.startActivity(this);
                } else {
                    Toast.makeText(AddDiaryActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.add_diary_ib_aa:
                mSeekBar.setVisibility(View.VISIBLE);
                mColorPick.setVisibility(View.VISIBLE);
                break;
            case R.id.add_diary_ib_pifu:
                // 如果权限已经申请过，直接进行图片选择
                getFromGallery(REQUEST_IMAGE_GET_BACKGROUND);
                break;
            case R.id.add_diary_ib_pic:
                // 如果权限已经申请过，直接进行图片选择
                getFromGallery(REQUEST_IMAGE_GET);
//                mPhotoPopupWindow = new PhotoPopupWindow(AddDiaryActivity.this, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        // 文件权限申请
//                        if (ContextCompat.checkSelfPermission(AddDiaryActivity.this,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                                != PackageManager.PERMISSION_GRANTED) {
//                            // 权限还没有授予，进行申请
//                            ActivityCompat.requestPermissions(AddDiaryActivity.this,
//                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200); // 申请的 requestCode 为 200
//                        } else {
//                            // 如果权限已经申请过，直接进行图片选择
//                            mPhotoPopupWindow.dismiss();
//                            getFromGallery();
//                        }
//                    }
//                }, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        // 拍照及文件权限申请
//                        if (ContextCompat.checkSelfPermission(AddDiaryActivity.this,
//                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
//                                || ContextCompat.checkSelfPermission(AddDiaryActivity.this,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                                != PackageManager.PERMISSION_GRANTED) {
//                            // 权限还没有授予，进行申请
//                            ActivityCompat.requestPermissions(AddDiaryActivity.this,
//                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 300); // 申请的 requestCode 为 300
//                        } else {
//                            // 权限已经申请，直接拍照
//                            mPhotoPopupWindow.dismiss();
//                            getFromCamera();
//                        }
//                    }
//                });
//                View rootView = LayoutInflater.from(AddDiaryActivity.this).inflate(R.layout.activity_add_diary, null);
//                mPhotoPopupWindow.showAtLocation(rootView,
//                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.add_diary_ib_weather:

                break;

        }
    }

    /**
     * 保存日记到数据库
     * @param content
     */
    private void saveEditContent(String content) {
        String date = GetDate.getDate().toString();
        String date2 = GetDate.getDate2().toString();
        String time = GetDate.getTime();
        String week = GetDate.getWeek();
        String tag = String.valueOf(System.currentTimeMillis());
        TextTypeBean textTypeBean = new TextTypeBean();
        textTypeBean.setText_color(mAddDiaryEtContent.getCurrentTextColor());
        textTypeBean.setText_size(mAddDiaryEtContent.getTextSize());
        try {
            SharedPreferencesUtil.saveObjToSp(AddDiaryActivity.this,ADDDIARY_BACKGROUND, TEXT_TYPE, textTypeBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        if (bundle == null) {
            ContentValues values = new ContentValues();
            values.put("date", date);
            values.put("date2", date2);
            values.put("time", time);
            values.put("week", week);
            values.put("weather", "晴");
            values.put("tag", tag);
            values.put("content", content);
            db.insert("Diary", null, values);
            values.clear();
        } else {
            //实例化内容值 ContentValues values = new ContentValues();
            ContentValues values = new ContentValues();
            values.put("content", content);
            //修改条件
            String whereClause = "tag=?";
            DiaryBean mBean = (DiaryBean) bundle.getSerializable("diaryObj");
            String _tag = mBean.getTag();
            //修改添加参数
            String[] whereArgs=new String[]{_tag};
            //修改
            db.update("Diary",values,whereClause,whereArgs);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        final String contentBack = mAddDiaryEtContent.getText().toString();
        if (!contentBack.trim().isEmpty()) {
            // 自动保存
            saveEditContent(contentBack);
            MainActivity.startActivity(AddDiaryActivity.this);
        } else {
            MainActivity.startActivity(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                }
                break;
            case 300:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                }
                break;
        }
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getFromCamera() {
        imageUriFromCamera = createImagePathUri(this);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // MediaStore.EXTRA_OUTPUT参数不设置时,系统会自动生成一个uri,但是只会返回一个缩略图
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera);//注意这一行
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    private void getFromGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ContentResolver resolver = getContentResolver();
        // 回调成功
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 相册选取
                case REQUEST_IMAGE_GET:
                    Uri originalUri = data.getData();
                    Log.e(TAG, originalUri+"");
                    Bitmap originalBitmap = null;
                    try {
                        originalBitmap = BitmapFactory.decodeStream(resolver.openInputStream(originalUri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (originalBitmap != null) {
                        insertImage(originalBitmap, originalUri);
                    } else {
                        Toast.makeText(AddDiaryActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                // 相册选取-皮肤
                case REQUEST_IMAGE_GET_BACKGROUND:
                    Uri originalUri1 = data.getData();
                    Bitmap bm = null;
                    try {
                        bm = BitmapFactory.decodeStream(resolver.openInputStream(originalUri1));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (bm != null) {
                        int bwidth = bm.getWidth();
                        int bHeight = bm.getHeight();
                        int width = ScreenUtils.getScreenWidth(AddDiaryActivity.this);
                        int height = width * bHeight / bwidth;
                        bm = ImageUtils.resizeImage(bm, width, height);
                        BitmapDrawable bd = new BitmapDrawable(this.getResources(), bm);
                        bd.setTileModeXY(Shader.TileMode.REPEAT , Shader.TileMode.REPEAT );
                        mAddDiaryPage.setBackground(bd);
//                        saveBitmapToSp(AddDiaryActivity.this, bm);
                        SharedPreferencesUtil.saveBitmapToSp(AddDiaryActivity.this,ADDDIARY_BACKGROUND, BITMAP,bm);
                    } else {
                        Toast.makeText(AddDiaryActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                // 拍照
                case REQUEST_IMAGE_CAPTURE:
//                    Uri uri= PictureUtil.getImageUri(this,data);
                    Uri uri= imageUriFromCamera;
                    Bitmap originalBitmap1 = null;
                    try {
                        originalBitmap1 = BitmapFactory.decodeStream(resolver.openInputStream(uri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (originalBitmap1 != null) {
                        insertImage(originalBitmap1, uri);
                    } else {
                        Toast.makeText(AddDiaryActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
                    }
//                    Bundle extras = data.getExtras();
//                    Bitmap originalBitmap1 = (Bitmap) extras.get("data");
                    break;
                default:
                    break;
            }
        } else {
        }
    }

    /**
     * 添加图片到EditText
     * @param bm
     * @param uri
     */
    private void insertImage(Bitmap bm,Uri uri) {
        int bwidth = bm.getWidth();
        int bHeight = bm.getHeight();
        int width = ScreenUtils.getScreenWidth(AddDiaryActivity.this);
        int height = width * bHeight / bwidth;
        bm = ImageUtils.resizeImage(bm, width, height);
        ImageSpan imageSpan = new ImageSpan(AddDiaryActivity.this, bm);

        String path = "[local]" + ImageUtils.getRealPathFromUri(AddDiaryActivity.this, uri) + "[/local]";
        Log.e(TAG, path);
        SpannableString spannableString = new SpannableString(path);
        spannableString.setSpan(imageSpan, 0, path.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        int start = mAddDiaryEtContent.getSelectionStart();//获取光标所在位置
        Editable editable = mAddDiaryEtContent.getEditableText();//获取Edittext中的内容
        if (start < 0 || start >= editable.length()) {
            editable.append(spannableString);
        } else {
            editable.insert(start, spannableString);
        }
    }

    /**
     * 创建一条图片地址uri,用于保存拍照后的照片
     * @param context
     * @return 图片的uri
     */

    private static Uri createImagePathUri(Context context) {
        final Uri[] imageFilePath = {null};
        //拍照前保存一条uri的地址
        String status = Environment.getExternalStorageState();
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = "IMG_" +  timeFormatter.format(new Date(time));
        String fileName;
        String parentPath;
        if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
            parentPath = Environment.getExternalStorageDirectory().getPath();
        } else {
            parentPath = context.getExternalCacheDir().getPath();
        }
        fileName = parentPath + File.separator + "/Download/" + imageName + ".jpg";
        imageFilePath[0] = getUriForFile(context, new File(fileName));

        Log.i(TAG, "生成的照片输出路径：" + imageFilePath[0].toString());
        return imageFilePath[0];
    }

    // Android 7.0 拍照权限问题
    private static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果SDK版本>=24，即：Build.VERSION.SDK_INT >= 24
            uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()+ ".provider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

}











