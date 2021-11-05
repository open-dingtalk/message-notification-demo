import react, { useEffect, useState } from "react"
import { Form, Input, Button, Checkbox, Upload, message } from "antd"
import { LoadingOutlined, PlusOutlined } from "@ant-design/icons"
import { ImageUploader } from "antd-mobile"
import axios from "axios"

function getBase64(img, callback) {
  const reader = new FileReader()
  reader.addEventListener("load", () => callback(reader.result))
  reader.readAsDataURL(img)
}

// function beforeUpload(file) {
//   const isJpgOrPng = file.type === "image/jpeg" || file.type === "image/png"
//   if (!isJpgOrPng) {
//     message.error("You can only upload JPG/PNG file!")
//   }
//   const isLt2M = file.size / 1024 / 1024 < 2
//   if (!isLt2M) {
//     message.error("Image must smaller than 2MB!")
//   }
//   return isJpgOrPng && isLt2M
// }

const uploadMedia = (data) => {
  // 上传图片
  // 发起会议
  const domain = ""
  axios({
    url: domain + "/upload",
    method: "post",
    data: data,
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then(function (response) {
      // alert(JSON.stringify(response));
      console.log(response)
      sessionStorage.setItem("mediaId", response.data.data)
    })
    .catch(function (error) {
      console.log(error)
    })
}

const Group = (props) => {
  const [form] = Form.useForm()

  const [loading, setLoading] = useState(false)
  const [imageUrl, setImageUrl] = useState("")

  const uploadButton = (
    <div>
      {loading ? <LoadingOutlined /> : <PlusOutlined />}
      <div style={{ marginTop: 8 }}>Upload</div>
    </div>
  )
  const handleChange = (info) => {
   
    if (info.file.status === "uploading") {
      setLoading(true)
      return
    }
    if (info.file.status === "done") {
      getBase64(info.file.originFileObj, (file) => {
        setImageUrl(file)
        setLoading(false)
      })
    }
  }

  useEffect(() => {
    // form.setFieldsValue({
    //   title: "任务待办",
    //   url: "/toWork",
    //   createTime: "2021-07-05 16:00:00",
    //   formTitle: "标题",
    //   formContent: "内容",
    // })
  }, [])

  const onSubmit = (data) => {
    const userId = sessionStorage.getItem("userId")
    console.log(form.validateFields((err,value)=>{
      console.log(err,'======',value)
    }), "-----")
    return
    const { userIdList, messageUrl, title, text, file } = data
    const picUrl = file?.file?.response?.data
    const params = {
      owner: userId,
      name: "群消息",
      userIdList,
      dingTalkMessage: {
        msgType: "link",
        link: {
          messageUrl,
          picUrl,
          title,
          text,
        },
      },
    }

    props.onClick(params)
  }

  return (
    <div>
      <h4 className="title">创建群消息</h4>
      <Form form={form} onFinish={onSubmit}>
        <Form.Item label="群名称" name="name" rules={[{required:true,message:'群名称必填'}]}>
          <Input placeholder="请输入群名称" />
        </Form.Item>
        <Form.Item label="消息标题" name="title" rules={[{required:true,message:'消息标题必填'}]}>
          <Input placeholder="请输入消息标题" />
        </Form.Item>
        <Form.Item label="消息内容" name="text" rules={[{required:true,message:'消息内容必填'}]}>
          <Input placeholder="请输入消息内容" />
        </Form.Item>
        <Form.Item label="消息链接" name="messageUrl" rules={[{required:true,message:'消息链接必填'}]}>
          <Input placeholder="请输入消息链接" />
        </Form.Item>
        <Form.Item label="消息图片" name="file" rules={[{required:true,message:'消息图片必填'}]}>
          <Upload
            listType="picture-card"
            className="avatar-uploader"
            showUploadList={false}
            action="/upload"
            // https://www.mocky.io/v2/5cc8019d300000980a055e76
            // beforeUpload={beforeUpload}
            onChange={handleChange}
          >
            {imageUrl ? (
              <img src={imageUrl} alt="avatar" style={{ width: "100%" }} />
            ) : (
              uploadButton
            )}
          </Upload>
        </Form.Item>
        <Form.Item label="选择发送人" name="userIdList" rules={[{required:true,message:'发送人必选'}]}>
          <Checkbox.Group>
            {/* onChange={onChange} */}
            {/*  [
              { name: "11111", userid: "22222" },
              { name: "333", userid: "444" },
            ]*/}
            {props.userIdList.map((item, i) => (
              <div key={"userid" + i}>
                <Checkbox value={item.userId} name={item.name}>
                  {item.name}
                </Checkbox>
              </div>
            ))}
          </Checkbox.Group>
        </Form.Item>
        <Button htmlType="submit" type="primary">
          提交
        </Button>
      </Form>
    </div>
  )
}

export default Group
