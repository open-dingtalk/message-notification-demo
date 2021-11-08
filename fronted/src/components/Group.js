import react, { useState } from "react"
import { Form, Input, Button, Checkbox, Upload, message } from "antd"
import { LoadingOutlined, PlusOutlined } from "@ant-design/icons"

function getBase64(img, callback) {
  const reader = new FileReader()
  reader.addEventListener("load", () => callback(reader.result))
  reader.readAsDataURL(img)
}

function beforeUpload(file) {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'|| file.type === 'image/gif'|| file.type === 'image/bmp';
  if (!isJpgOrPng) {
    message.error('图片类型支持jpg/png/gif/bmp格式');
  }
  const isLt2M = file.size / 1024 / 1024 < 1;
  if (!isLt2M) {
    message.error('图片最大上传1M!');
  }
  return isJpgOrPng && isLt2M;
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

  const onSubmit = async () => {
    const userId = sessionStorage.getItem("userId")
    await form.validateFields().then((values) => {
      const { userIdList, messageUrl, title, text, file, name } = values
      const picUrl = file?.file?.response?.data
      const params = {
        owner: userId,
        name,
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
    })
  }

  return (
    <div>
      <a onClick={props.onClose}>←返回</a>
      <br />
      <br />
      <h4 className="title">创建群消息</h4>
      <Form form={form} onFinish={onSubmit}>
        <Form.Item
          label="群名称"
          name="name"
          rules={[{ required: true, message: "群名称必填" }]}
        >
          <Input placeholder="请输入群名称" />
        </Form.Item>
        <Form.Item
          label="消息标题"
          name="title"
          rules={[{ required: true, message: "消息标题必填" }]}
        >
          <Input placeholder="请输入消息标题" />
        </Form.Item>
        <Form.Item
          label="消息内容"
          name="text"
          rules={[{ required: true, message: "消息内容必填" }]}
        >
          <Input placeholder="请输入消息内容" />
        </Form.Item>
        <Form.Item
          label="消息链接"
          name="messageUrl"
          rules={[{ required: true, message: "消息链接必填" }]}
        >
          <Input placeholder="请输入消息链接" />
        </Form.Item>
        <Form.Item
          label="消息图片"
          name="file"
          rules={[{ required: true, message: "消息图片必填" }]}
        >
          <Upload
            listType="picture-card"
            className="avatar-uploader"
            showUploadList={false}
            action="/upload"
            beforeUpload={beforeUpload}
            onChange={handleChange}
            style={{ overflow: "hidden" }}
          >
            {imageUrl ? (
              <img src={imageUrl} alt="avatar" style={{ width: "100%" }} />
            ) : (
              uploadButton
            )}
          </Upload>
        </Form.Item>
        <Form.Item
          label="选择发送人"
          name="userIdList"
          rules={[{ required: true, message: "发送人必选" }]}
        >
          <Checkbox.Group>
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
