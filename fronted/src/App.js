import react, { useEffect, useState } from "react"
import "./App.css"
import axios from "axios"
import * as dd from "dingtalk-jsapi"
import { Button, message } from "antd"
import Group from "./components/Group"
import ReadedUsers from "./components/ReadedUsers"
import "antd/dist/antd.min.css"

//内网穿透工具介绍:
// https://developers.dingtalk.com/document/resourcedownload/http-intranet-penetration?pnamespace=app
// 替换成后端服务域名
export const domain = ""

function App() {
  const [showType, setShowType] = useState(0)
  const [userIdList, setUserIdList] = useState([])
  const [readedUserList, setReadedUserList] = useState([])
  useEffect(() => {
    dd.ready(function () {
      let corpId
      fetch(domain + "/config")
        .then((res) => res.json())
        .then((result) => {
          corpId = result.data.corpId
          // dd.ready参数为回调函数，在环境准备就绪时触发，jsapi的调用需要保证在该回调函数触发后调用，否则无效。
          dd.runtime.permission.requestAuthCode({
            corpId: corpId, //三方企业ID
            onSuccess: function (result) {
              axios
                .get(domain + "/login?authCode=" + result.code)
                .then((response) => {
                  // 登录成功后储存用户部门和ID
                  sessionStorage.setItem("userId", response.data.data.userid)
                  sessionStorage.setItem("unionId", response.data.data.unionid)
                  sessionStorage.setItem(
                    "deptId",
                    response.data.data.deptIdList[0]
                  )
                  message.success("登陆成功：" + response.data.data.userName)
                  const qs = require("qs")
                  // 获取部门部门用户详情
                  axios
                    .get(domain + "/users", {
                      params: {
                        deptIds: response.data.data.deptIdList,
                      },
                      paramsSerializer: function (params) {
                        return qs.stringify(params, { arrayFormat: "repeat" })
                      },
                    })
                    .then((response) => {
                      // 此处为创建人的userId，可以使用response里面返回的数据
                      setUserIdList(response.data.data)
                    })
                    .catch((error) => {
                      alert(JSON.stringify(error))
                    })
                })
                .catch((error) => {
                  alert(JSON.stringify(error))
                })
            },
            onFail: function (err) {
              alert(JSON.stringify(err))
            },
          })
        })
    })
  }, [])

  const sendGroupMessage = (data) => {
    // 发送群消息
    axios({
      url: domain + "/message/group",
      method: "post",
      data: JSON.stringify(data),
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then(function (response) {
        if (response.data.success) {
          message.success("发送群消息成功")
          setShowType(0)
          sessionStorage.setItem("messageId", response.data.data)
        }
      })
      .catch(function (error) {
        console.log(error)
      })
  }

  const readUserList = async () => {
    if (!sessionStorage.getItem("messageId")) {
      message.error("请先创建群消息")
      return
    }
    // 获取已读人员列表
    axios
      .get(domain + "/message/group/" + sessionStorage.getItem("messageId"))
      .then(function (response) {
        setReadedUserList(response.data.data)
      })
      .catch(function (error) {
        console.log(error)
      })
    await setShowType(2)
  }

  return (
    <div className="content">
      <div className="header">
        <img
          src="https://img.alicdn.com/imgextra/i3/O1CN01Mpftes1gwqxuL0ZQE_!!6000000004207-2-tps-240-240.png"
          className="headImg"
        />
        钉钉 demo
      </div>

      <div className="App">
        {showType === 1 && (
          <Group
            onClick={(params) => sendGroupMessage(params)}
            userIdList={userIdList}
          />
        )}
        <div>
          {!showType && (
            <Button type="primary" onClick={() => setShowType(1)}>
              发送群消息
            </Button>
          )}
          <br />
          <br />
          {!showType && (
            <Button type="primary" onClick={readUserList}>
              查看已读人员列表
            </Button>
          )}
        </div>
        {showType === 2 && (
          <div>
            <ReadedUsers readedUserList={readedUserList} />
          </div>
        )}
      </div>
    </div>
  )
}

export default App
