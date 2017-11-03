package com.superschool.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.GFImageView;

/**
 * Created by xiaohao on 17-11-3.
 */

public class ImagePicker {


    ThemeConfig theme;
    FunctionConfig functionConfig;
    MImageLoader imageLoader;
    CoreConfig coreConfig;


    public void init(Context context)
     {

         theme=new ThemeConfig.Builder().build();
         functionConfig=new FunctionConfig.Builder().setEnablePreview(true)
                 .setEnableCamera(true).setEnableEdit(true).build();
         imageLoader=new MImageLoader();
         coreConfig=new CoreConfig.Builder(context,imageLoader,theme).setFunctionConfig(functionConfig).build();

     }
     public void pick(int sum){
         GalleryFinal.init(coreConfig);
         GalleryFinal.openGalleryMuti(1, sum, new GalleryFinal.OnHanlderResultCallback() {
             @Override
             public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                 System.out.println(resultList.get(0).getPhotoPath());


             }

             @Override
             public void onHanlderFailure(int requestCode, String errorMsg) {

             }
         });
     }

}
