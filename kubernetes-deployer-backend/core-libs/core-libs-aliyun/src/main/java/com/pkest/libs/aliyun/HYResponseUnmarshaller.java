package com.pkest.libs.aliyun;

import com.aliyuncs.transform.UnmarshallerContext;
import com.pkest.libs.common.util.GsonUtils;

/**
 * Created by wuzhonggui on 2018/11/14.
 * QQ: 2731429978
 * Email: pk8est@qq.com
 */
public class HYResponseUnmarshaller {

    public HYResponseUnmarshaller() {
    }

    public static HYResponse unmarshall(HYResponse hyResponse, UnmarshallerContext context) {

        GsonUtils.getGson().toJson(context);
        /*hyResponse.setRequestId(context.stringValue("CreateLiveStreamRecordIndexFilesResponse.RequestId"));
        RecordInfo recordInfo = new RecordInfo();
        recordInfo.setRecordId(context.stringValue("CreateLiveStreamRecordIndexFilesResponse.RecordInfo.RecordId"));
        recordInfo.setRecordUrl(context.stringValue("CreateLiveStreamRecordIndexFilesResponse.RecordInfo.RecordUrl"));
        recordInfo.setDomainName(context.stringValue("CreateLiveStreamRecordIndexFilesResponse.RecordInfo.DomainName"));
        recordInfo.setAppName(context.stringValue("CreateLiveStreamRecordIndexFilesResponse.RecordInfo.AppName"));
        recordInfo.setStreamName(context.stringValue("CreateLiveStreamRecordIndexFilesResponse.RecordInfo.StreamName"));
        recordInfo.setOssBucket(context.stringValue("CreateLiveStreamRecordIndexFilesResponse.RecordInfo.OssBucket"));
        recordInfo.setOssEndpoint(context.stringValue("CreateLiveStreamRecordIndexFilesResponse.RecordInfo.OssEndpoint"));
        recordInfo.setOssObject(context.stringValue("CreateLiveStreamRecordIndexFilesResponse.RecordInfo.OssObject"));
        recordInfo.setStartTime(context.stringValue("CreateLiveStreamRecordIndexFilesResponse.RecordInfo.StartTime"));
        recordInfo.setEndTime(context.stringValue("CreateLiveStreamRecordIndexFilesResponse.RecordInfo.EndTime"));
        recordInfo.setDuration(context.floatValue("CreateLiveStreamRecordIndexFilesResponse.RecordInfo.Duration"));
        recordInfo.setHeight(context.integerValue("CreateLiveStreamRecordIndexFilesResponse.RecordInfo.Height"));
        recordInfo.setWidth(context.integerValue("CreateLiveStreamRecordIndexFilesResponse.RecordInfo.Width"));
        recordInfo.setCreateTime(context.stringValue("CreateLiveStreamRecordIndexFilesResponse.RecordInfo.CreateTime"));
        createLiveStreamRecordIndexFilesResponse.setRecordInfo(recordInfo);
        return createLiveStreamRecordIndexFilesResponse;*/
        return hyResponse;
    }
}
