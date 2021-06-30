import './App.css';
import axios from 'axios';
import * as dd from 'dingtalk-jsapi';

//内网穿透工具介绍:
// https://developers.dingtalk.com/document/resourcedownload/http-intranet-penetration?pnamespace=app
// 替换成后端服务域名
export const domain = "";

function App() {

    const sendGroupMessage = () => {
        // 获取存储的用户部门和ID
        const userId = sessionStorage.getItem('userId');
        const unionId = sessionStorage.getItem('unionId');
        const inviteUnionId = sessionStorage.getItem('inviteUnionId');
        const deptId = sessionStorage.getItem('deptId');
        // demo直接构建了要请求的数据，实际开发需要从页面获取
        const data = {
            "owner": userId,
            "confTitle": "会议Demo",
            "inviteUserIds": [inviteUnionId]
        };
        // 发起会议
        axios({
            url: domain + '/meeting',
            method: 'post',
            data: data,
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(function (response) {
                // alert(JSON.stringify(response));
                console.log(response);
                sessionStorage.setItem("conferenceId", response.data.result.conferenceId);

            })
            .catch(function (error) {
                console.log(error);
            });
    };

    const readUserList = () => {
        // 获取存储的用户部门和ID
        const unionId = sessionStorage.getItem('unionId');
        const conferenceId = sessionStorage.getItem('conferenceId');

        // 关闭会议
        axios({
            url: domain + '/meeting?conferenceId=' + conferenceId + '&unionId=' + unionId,
            method: 'put',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(function (response) {

            })
            .catch(function (error) {
                console.log(error);
            });
    };

    return (
        <div className="App">
            {/*<header className="App-header">*/}
            {/*<button onClick={createMeeting}>领用并提交审批</button>*/}
            {/*</header>*/}
            <header className="App-header">
            <button onClick={sendGroupMessage}>发送群消息</button>
            <button onClick={readUserList}>查看已读人员列表</button>
            </header>
            {/*<div className="container">*/}
            {/*    <List/>*/}
            {/*</div>*/}
        </div>
    );
};


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
    let corpId;
    fetch(domain + '/config')
        .then(res => res.json())
        .then((result) => {
            // alert(JSON.stringify(result));
            corpId = result.result.corpId;
            // dd.ready参数为回调函数，在环境准备就绪时触发，jsapi的调用需要保证在该回调函数触发后调用，否则无效。
            dd.runtime.permission.requestAuthCode({

                corpId: corpId, //三方企业ID
                onSuccess: function (result) {
                    // alert(JSON.stringify(result));
                    axios.get(domain + "/login?authCode=" + result.code)
                        .then(response => {
                            // alert(JSON.stringify(response));
                            // alert(JSON.stringify(response.data));
                            // alert(JSON.stringify(response.data.result.userid));
                            // alert(JSON.stringify(response.data.result.deptIdList[0]));
                            // 登录成功后储存用户部门和ID
                            sessionStorage.setItem("userId", response.data.result.userid);
                            sessionStorage.setItem("unionId", response.data.result.unionid);
                            sessionStorage.setItem("deptId", response.data.result.deptIdList[0]);
                            const qs = require('qs');
                            axios.get(domain + "/users", {
                                params: {
                                    deptIds: response.data.result.deptIdList,
                                },
                                paramsSerializer: function (params) {
                                    return qs.stringify(params, {arrayFormat: 'repeat'});
                                }
                            }).then(response => {
                                console.log(response);
                                // 此处为硬编码邀请人的unionid，可以使用response里面返回的数据
                                sessionStorage.setItem("inviteUnionId", "***");
                            }).catch(error => {
                                alert(JSON.stringify(error));
                            })
                        })
                        .catch(error => {
                            alert(JSON.stringify(error));
                            // console.log(error.message)
                        })

                },
                onFail: function (err) {
                    alert(JSON.stringify(err))
                }
            });
        });
});

export default App;
