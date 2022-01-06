import react from "react"
import { Table } from "antd"

const ReadedUsers = (props) => {
  const column = [
    {
      title: "用户id",
      dataIndex: "userId",
      key: "userId",
    },
  ]
  return (
    <div>
      <a onClick={props.onClose}>←返回</a>
      <br />
      <br />
      <Table columns={column} dataSource={props.readedUserList} />
    </div>
  )
}

export default ReadedUsers
