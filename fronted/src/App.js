import react, { useEffect, useState } from "react"
import "./App.css"
import axios from "axios"
import * as dd from "dingtalk-jsapi"
import { Button } from "antd"
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
  const [readedUserList,setReadedUserList] = useState([])
  useEffect(() => {
    return
    dd.ready(function () {
      // let corpId;
      // axios.get(domain + "/config")
      //     .then(response => {
      //         corpId = response.data.corpId;
      //     })
      //     .catch(error => {
      //         alert(JSON.stringify(error))
      //         // console.log(error.message)
      //     })
      let corpId
      fetch(domain + "/config")
        .then((res) => res.json())
        .then((result) => {
          // alert(JSON.stringify(result));
          corpId = result.data.corpId
          // dd.ready参数为回调函数，在环境准备就绪时触发，jsapi的调用需要保证在该回调函数触发后调用，否则无效。
          dd.runtime.permission.requestAuthCode({
            corpId: corpId, //三方企业ID
            onSuccess: function (result) {
              // alert(JSON.stringify(result));
              axios
                .get(domain + "/login?authCode=" + result.code)
                .then((response) => {
                  // alert(JSON.stringify(response));
                  // alert(JSON.stringify(response.data));
                  // alert(JSON.stringify(response.data.data.userid));
                  // alert(JSON.stringify(response.data.data.deptIdList[0]));
                  // 登录成功后储存用户部门和ID
                  sessionStorage.setItem("userId", response.data.data.userid)
                  sessionStorage.setItem("unionId", response.data.data.unionid)
                  sessionStorage.setItem(
                    "deptId",
                    response.data.data.deptIdList[0]
                  )
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
                      console.log(response)
                      // 此处为创建人的userId，可以使用response里面返回的数据
                      //   sessionStorage.setItem(
                      //     "userIdList",
                      //     sessionStorage.getItem("userId")
                      //   )
                      setUserIdList(response.data.data)
                    })
                    .catch((error) => {
                      alert(JSON.stringify(error))
                    })
                })
                .catch((error) => {
                  alert(JSON.stringify(error))
                  // console.log(error.message)
                })
            },
            onFail: function (err) {
              alert(JSON.stringify(err))
            },
          })
        })
    })
  }, [])

  //   const uploadMedia = () => {
  //     // 上传图片
  //     const data = {}
  //     // 发起会议
  //     axios({
  //       url: domain + "/upload",
  //       method: "post",
  //       data: data,
  //       headers: {
  //         "Content-Type": "application/json",
  //       },
  //     })
  //       .then(function (response) {
  //         // alert(JSON.stringify(response));
  //         console.log(response)
  //         sessionStorage.setItem("mediaId", response.data.data)
  //       })
  //       .catch(function (error) {
  //         console.log(error)
  //       })
  //   }

  const sendGroupMessage = (data) => {
    // 发送群消息
    // const userId = sessionStorage.getItem("userId")
    // demo直接构建了要请求的数据，实际开发需要从页面获取
    // let url = "https://www.dingtalk.com"
    // {
    //     owner: userId,
    //     name: "群消息",
    //     userIdList,
    //     dingTalkMessage: {
    //       msgType: "link",
    //       link: {
    //         messageUrl,
    //         picUrl,
    //         title,
    //         text,
    //       },
    //     },
    //   }
    // const data = {
    //   owner: userId,
    //   name: "群消息",
    //   userIdList: [userIdList],
    //   dingTalkMessage: {
    //     msgType: "link",
    //     link: {
    //       messageUrl:
    //         "dingtalk://dingtalkclient/page/link?url=" +
    //         encodeURIComponent(url) +
    //         "&pc_slide=true",
    //       picUrl: sessionStorage.getItem("mediaId"),
    //       title: "测试",
    //       text: "测试",
    //     },
    //   },
    // }
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
        // alert(JSON.stringify(response));
        setShowType(0)
        console.log(response)
        sessionStorage.setItem("messageId", response.data.data)
      })
      .catch(function (error) {
        console.log(error)
      })
  }

  const readUserList = async () => {
    // 获取已读人员列表
    axios
      .get(domain + "/message/group/" + sessionStorage.getItem("messageId"))
      .then(function (response) {
        setReadedUserList(response.data.data)
        alert(JSON.stringify(response.data.data))
      })
      .catch(function (error) {
        console.log(error)
      })
    await setShowType(2)
  }

  return (
    <div className="App">
      {/*<header className="App-header">*/}
      {/*<button onClick={createMeeting}>领用并提交审批</button>*/}
      {/*</header>*/}
      {showType === 1 && (
        <Group
          onClick={(params) => sendGroupMessage(params)}
          userIdList={userIdList}
        />
      )}
      <div>
        {/* <Button onClick={uploadMedia}>上传媒体文件</Button> */}
        {!showType && (
          <Button type="primary" onClick={() => setShowType(1)}>
            发送群消息
          </Button>
        )}
        <br/><br/>
        {!showType && (
          <Button type="primary" onClick={readUserList}>
            查看已读人员列表
          </Button>
        )}
      </div>
      {showType===2&&<div>
          <ReadedUsers readedUserList={readedUserList}/>
      </div>}
     
    </div>
  )
}

export default App
