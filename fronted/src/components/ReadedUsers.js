import react from 'react'
import {Table} from 'antd'

const ReadedUsers = (props)=>{
    const column = [{
        title: '用户id',
        dataIndex: 'userId',
        key: 'userId',
      }]
    return <Table columns={column} dataSource={props.readedUserList}/>
}

export default ReadedUsers