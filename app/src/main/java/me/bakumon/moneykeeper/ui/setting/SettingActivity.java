package me.bakumon.moneykeeper.ui.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.Router;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.databinding.ActivitySettingBinding;
import me.bakumon.moneykeeper.utill.AlipayZeroSdk;
import me.bakumon.moneykeeper.utill.BackupUtil;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.drakeet.floo.Floo;

/**
 * 设置
 *
 * @author Bakumon https://bakumon.me
 */
public class SettingActivity extends BaseActivity {

    private ActivitySettingBinding mBinding;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();

        initView();
    }

    private void initView() {
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.titleBar.setTitle(getString(R.string.text_title_setting));

        mBinding.rvSetting.setLayoutManager(new LinearLayoutManager(this));
        SettingAdapter adapter = new SettingAdapter(null);

        List<SettingSectionEntity> list = new ArrayList<>();

        list.add(new SettingSectionEntity("记账"));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item("极速记账", "打开应用立即记账", true)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item("收支类型管理", null, false)));

        list.add(new SettingSectionEntity("备份"));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item("立即备份", "备份文件将保存到sdcard/Backup/moneykeeper", false)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item("恢复备份", "备份文件将从sdcard/Backup/moneykeeper读取", false)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item("实时备份", "数据有改变自动备份(建议开启)", true)));

        list.add(new SettingSectionEntity("关于|帮助"));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item("关于", "了解我们的设计理念", false)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item("评分", "给个好评呗\uD83D\uDE18", false)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item("捐赠作者", "", false)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item(null, "隐私政策", false)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item(null, "开源许可证", false)));

        adapter.setNewData(list);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            switch (position) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    goTypeManage();
                    break;
                case 3:
                    break;
                case 4:
                    backupDB();
                    break;
                case 5:
                    restoreDB();
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    market();
                    break;
                case 10:
                    alipay();
                    break;
                case 11:
                    break;
                case 12:
                    break;
                default:
                    break;
            }
        });
        mBinding.rvSetting.setAdapter(adapter);
    }

    private void backupDB() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(permissions -> {
                    BackupUtil.autoBackup();
                })
                .onDenied(permissions -> {
                    ToastUtils.show("备份数据需要开启读写权限");
                })
                .start();
    }

    private void restoreDB() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(permissions -> {
                    BackupUtil.restoreDB();
                    Floo.stack(this)
                            .target(Router.IndexKey.INDEX_KEY_HOME)
                            .result("refresh")
                            .start();
                })
                .onDenied(permissions -> {
                    ToastUtils.show("备份数据需要开启读写权限");
                })
                .start();
    }

    private void goTypeManage() {
        Floo.navigation(this, Router.Url.URL_TYPE_MANAGE)
                .start();
    }

    private void market() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            ToastUtils.show(R.string.toast_not_install_market);
            e.printStackTrace();
        }
    }

    private void alipay() {
        // https://fama.alipay.com/qrcode/qrcodelist.htm?qrCodeType=P  二维码地址
        // http://cli.im/deqr/ 解析二维码
        // aex01251c8foqaprudcp503
        if (AlipayZeroSdk.hasInstalledAlipayClient(this)) {
            AlipayZeroSdk.startAlipayClient(this, "aex01251c8foqaprudcp503");
        } else {
            ToastUtils.show(R.string.toast_not_install_alipay);
        }
    }
}
