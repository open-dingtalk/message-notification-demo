(this.webpackJsonpfronted=this.webpackJsonpfronted||[]).push([[0],{192:function(e,t,n){},193:function(e,t,n){},504:function(e,t,n){"use strict";n.r(t);var a=n(0),s=n.n(a),i=n(28),r=n.n(i),c=(n(192),n(81)),o=n.n(c),l=n(112),d=n(38),u=(n(193),n(36)),j=n.n(u),f=n(83),h=n(52),m=n(508),p=n(510),b=n(509),O=n(512),g=n(34),x=n(513),I=(n(505),n(10));var v=function(e){var t=m.a.useForm(),n=Object(d.a)(t,1)[0],s=Object(a.useState)(!1),i=Object(d.a)(s,2),r=i[0],c=i[1],o=Object(a.useState)(""),l=Object(d.a)(o,2),u=l[0],j=l[1],f=Object(I.jsxs)("div",{children:[r?Object(I.jsx)(g.a,{}):Object(I.jsx)(x.a,{}),Object(I.jsx)("div",{style:{marginTop:8},children:"Upload"})]});Object(a.useEffect)((function(){}),[]);return Object(I.jsxs)("div",{children:[Object(I.jsx)("h4",{className:"title",children:"\u521b\u5efa\u7fa4\u6d88\u606f"}),Object(I.jsxs)(m.a,{form:n,onFinish:function(t){var n,a,s=sessionStorage.getItem("userId");console.log(t,"-----");var i=t.userIdList,r=t.messageUrl,c=t.title,o=t.text,l=t.file,d={owner:s,name:"\u7fa4\u6d88\u606f",userIdList:i,dingTalkMessage:{msgType:"link",link:{messageUrl:r,picUrl:null===l||void 0===l||null===(n=l.file)||void 0===n||null===(a=n.response)||void 0===a?void 0:a.url,title:c,text:o}}};e.onClick(d)},children:[Object(I.jsx)(m.a.Item,{label:"\u7fa4\u540d\u79f0",name:"name",children:Object(I.jsx)(p.a,{placeholder:"\u8bf7\u8f93\u5165\u7fa4\u540d\u79f0"})}),Object(I.jsx)(m.a.Item,{label:"\u6d88\u606f\u6807\u9898",name:"title",children:Object(I.jsx)(p.a,{placeholder:"\u8bf7\u8f93\u5165\u6d88\u606f\u6807\u9898"})}),Object(I.jsx)(m.a.Item,{label:"\u6d88\u606f\u5185\u5bb9",name:"text",children:Object(I.jsx)(p.a,{placeholder:"\u8bf7\u8f93\u5165\u6d88\u606f\u5185\u5bb9"})}),Object(I.jsx)(m.a.Item,{label:"\u6d88\u606f\u94fe\u63a5",name:"messageUrl",children:Object(I.jsx)(p.a,{placeholder:"\u8bf7\u8f93\u5165\u6d88\u606f\u94fe\u63a5"})}),Object(I.jsx)(m.a.Item,{label:"\u6d88\u606f\u56fe\u7247",name:"file",children:Object(I.jsx)(b.a,{listType:"picture-card",className:"avatar-uploader",showUploadList:!1,action:"/upload",onChange:function(e){"uploading"!==e.file.status?"done"===e.file.status&&function(e,t){var n=new FileReader;n.addEventListener("load",(function(){return t(n.result)})),n.readAsDataURL(e)}(e.file.originFileObj,(function(e){j(e),c(!1)})):c(!0)},children:u?Object(I.jsx)("img",{src:u,alt:"avatar",style:{width:"100%"}}):f})}),Object(I.jsx)(m.a.Item,{label:"\u9009\u62e9\u53d1\u9001\u4eba",name:"userIdList",children:Object(I.jsx)(O.a.Group,{children:e.userIdList.map((function(e,t){return Object(I.jsx)("div",{children:Object(I.jsx)(O.a,{value:e.userId,name:e.name,children:e.name})},"userid"+t)}))})}),Object(I.jsx)(h.a,{htmlType:"submit",type:"primary",children:"\u63d0\u4ea4"})]})]})};n(500);var y=function(){var e=Object(a.useState)(0),t=Object(d.a)(e,2),s=t[0],i=t[1],r=Object(a.useState)([]),c=Object(d.a)(r,2),u=c[0],m=c[1];Object(a.useEffect)((function(){f.ready((function(){var e;fetch("/config").then((function(e){return e.json()})).then((function(t){e=t.data.corpId,f.runtime.permission.requestAuthCode({corpId:e,onSuccess:function(e){j.a.get("/login?authCode="+e.code).then((function(e){sessionStorage.setItem("userId",e.data.data.userid),sessionStorage.setItem("unionId",e.data.data.unionid),sessionStorage.setItem("deptId",e.data.data.deptIdList[0]);var t=n(501);j.a.get("/users",{params:{deptIds:e.data.data.deptIdList},paramsSerializer:function(e){return t.stringify(e,{arrayFormat:"repeat"})}}).then((function(e){console.log(e),m(e.data.data)})).catch((function(e){alert(JSON.stringify(e))}))})).catch((function(e){alert(JSON.stringify(e))}))},onFail:function(e){alert(JSON.stringify(e))}})}))}))}),[]);var p=function(){var e=Object(l.a)(o.a.mark((function e(){return o.a.wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return j.a.get("/message/group/"+sessionStorage.getItem("messageId")).then((function(e){m(e.data.data),alert(JSON.stringify(e.data.data))})).catch((function(e){console.log(e)})),e.next=3,i(2);case 3:case"end":return e.stop()}}),e)})));return function(){return e.apply(this,arguments)}}();return Object(I.jsxs)("div",{className:"App",children:[1===s&&Object(I.jsx)(v,{onClick:function(e){return t=e,void j()({url:"/message/group",method:"post",data:JSON.stringify(t),headers:{"Content-Type":"application/json"}}).then((function(e){i(!1),console.log(e),sessionStorage.setItem("messageId",e.data.data)})).catch((function(e){console.log(e)}));var t},userIdList:u}),Object(I.jsxs)("header",{className:"App-header",children:[!s&&Object(I.jsx)(h.a,{type:"primary",onClick:function(){return i(1)},children:"\u53d1\u9001\u7fa4\u6d88\u606f"}),!s&&Object(I.jsx)(h.a,{type:"primary",onClick:p,children:"\u67e5\u770b\u5df2\u8bfb\u4eba\u5458\u5217\u8868"})]})]})},S=function(e){e&&e instanceof Function&&n.e(3).then(n.bind(null,514)).then((function(t){var n=t.getCLS,a=t.getFID,s=t.getFCP,i=t.getLCP,r=t.getTTFB;n(e),a(e),s(e),i(e),r(e)}))};r.a.render(Object(I.jsx)(s.a.StrictMode,{children:Object(I.jsx)(y,{})}),document.getElementById("root")),S()}},[[504,1,2]]]);
//# sourceMappingURL=main.16909387.chunk.js.map