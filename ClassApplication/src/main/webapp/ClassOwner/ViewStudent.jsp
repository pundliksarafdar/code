<%@page import="com.classapp.db.batch.division.Division"%>
<meta http-equiv="cache-control" content="max-age=0" />
<%@page import="com.classapp.db.student.StudentDetails"%>
<%@page import="com.classapp.db.subject.Subject"%>
<%@page import="com.classapp.db.batch.Batch"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.config.Constants"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<html>
  <link rel="stylesheet" href="js/jquery.typeahead.min.css">
<style>
.scrollable-menu {
    height: auto;
    max-height: 200px;
    overflow-x: hidden;
}

.error{
color: red;
}

.typeahead-field,.typeahead-query{position:relative;width:100%}.typeahead-button,.typeahead-dropdown>li>a{white-space:nowrap}.typeahead-button,.typeahead-container,.typeahead-field,.typeahead-filter,.typeahead-query{position:relative}.typeahead-container button,.typeahead-field input,.typeahead-select{border:1px solid #ccc;line-height:1.42857143;padding:6px 12px;height:32px}button,input,optgroup,select,textarea{color:inherit;font:inherit;margin:0}.typeahead-container,.typeahead-result.detached .typeahead-list{font-family:"Open Sans",Arial,Helvetica,Sans-Serif}button{overflow:visible}button,select{text-transform:none}button,html input[type=button],input[type=reset],input[type=submit]{-webkit-appearance:button;cursor:pointer}button[disabled],html input[disabled]{cursor:default}button::-moz-focus-inner,input::-moz-focus-inner{border:0;padding:0}input{line-height:normal}.typeahead-container *{box-sizing:border-box}.typeahead-container *,.typeahead-field input{-webkit-box-sizing:border-box;-moz-box-sizing:border-box}.typeahead-query{z-index:2}.typeahead-filter button{min-width:66px}.typeahead-field{display:table;border-collapse:separate}.typeahead-button{font-size:0;width:1%;vertical-align:middle}.typeahead-field>span{display:table-cell;vertical-align:top}.typeahead-button button{border-top-right-radius:2px;border-bottom-right-radius:2px}.typeahead-field input,.typeahead-select{display:block;width:100%;font-size:13px;color:#555;background:0 0;border-radius:2px 0 0 2px;-webkit-box-shadow:inset 0 1px 1px rgba(0,0,0,.075);box-shadow:inset 0 1px 1px rgba(0,0,0,.075);-webkit-transition:border-color ease-in-out .15s,box-shadow ease-in-out .15s;-o-transition:border-color ease-in-out .15s,box-shadow ease-in-out .15s;transition:border-color ease-in-out .15s,box-shadow ease-in-out .15s}.typeahead-field input{-webkit-appearance:none;box-sizing:border-box}.typeahead-container .typeahead-field input{background:#fff}.typeahead-container.hint .typeahead-field input{background:0 0}.typeahead-container.hint .typeahead-field input:last-child,.typeahead-hint{background:#fff}.typeahead-container button{display:inline-block;margin-bottom:0;font-weight:400;text-align:center;vertical-align:middle;touch-action:manipulation;cursor:pointer;background-color:#fff;white-space:nowrap;font-size:13px;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;color:#333;box-shadow:inset 0 -2px 0 rgba(0,0,0,.05);-moz-box-shadow:inset 0 -2px 0 rgba(0,0,0,.05);-webkit-box-shadow:inset 0 -2px 0 rgba(0,0,0,.05)}.typeahead-container button:active,.typeahead-container button:focus{outline:dotted thin;outline:-webkit-focus-ring-color auto 5px;outline-offset:-2px}.typeahead-container button:focus,.typeahead-container button:hover{color:#333;background-color:#e6e6e6;border-color:#adadad}.typeahead-container button.active,.typeahead-container button:active{outline:0;background-image:none;-webkit-box-shadow:inset 0 3px 5px rgba(0,0,0,.125);box-shadow:inset 0 3px 5px rgba(0,0,0,.125)}.typeahead-container button.disabled,.typeahead-container button[disabled],.typeahead-field input.disabled,.typeahead-field input[disabled]{cursor:not-allowed;pointer-events:none;opacity:.65;filter:alpha(opacity=65);-webkit-box-shadow:none;box-shadow:none;background-color:#fff;border-color:#ccc}.typeahead-button button,.typeahead-filter button{margin-left:-1px;border-bottom-left-radius:0;border-top-left-radius:0}.typeahead-button,.typeahead-filter{z-index:1}.typeahead-button:active,.typeahead-button:active button:active,.typeahead-button:focus,.typeahead-button:focus button:focus,.typeahead-button:hover,.typeahead-container.filter .typeahead-filter,.typeahead-filter:active,.typeahead-filter:focus,.typeahead-filter:hover{z-index:1001}.typeahead-dropdown,.typeahead-list{position:absolute;top:100%;left:0;z-index:1000;width:100%;min-width:160px;padding:5px 0;margin:2px 0 0;list-style:none;font-size:13px;text-align:left;background-color:#fff;border:1px solid #ccc;border:1px solid rgba(0,0,0,.15);border-radius:2px;-webkit-box-shadow:0 6px 12px rgba(0,0,0,.175);box-shadow:0 6px 12px rgba(0,0,0,.175);background-clip:padding-box}.typeahead-result.detached .typeahead-list{position:relative;z-index:1041;top:auto;left:auto}.typeahead-dropdown{right:0;left:auto;z-index:1001}.typeahead-list>li:first-child{border-top:none}.typeahead-list>li{position:relative;border-top:solid 1px rgba(0,0,0,.15)}.typeahead-dropdown>li>a,.typeahead-list>li>a{display:block;padding:6px 20px;clear:both;font-weight:400;line-height:1.42857143;color:#333;text-decoration:none}.typeahead-dropdown>li.active>a,.typeahead-dropdown>li>a:focus,.typeahead-dropdown>li>a:hover,.typeahead-list>li.active>a,.typeahead-list>li>a:focus,.typeahead-list>li>a:hover{background-color:#ebebeb;color:#333}.typeahead-list.empty>li.active>a,.typeahead-list.empty>li>a:focus,.typeahead-list.empty>li>a:hover{background-color:transparent}.typeahead-list.empty>li>a{cursor:default}.typeahead-list>li.typeahead-group.active>a,.typeahead-list>li.typeahead-group>a,.typeahead-list>li.typeahead-group>a:focus,.typeahead-list>li.typeahead-group>a:hover{border-color:#9cb4c5;color:#305d8c;background-color:#d6dde7;cursor:default}.typeahead-container.backdrop+.typeahead-backdrop,.typeahead-container.filter .typeahead-dropdown,.typeahead-container.hint .typeahead-hint,.typeahead-container.result .typeahead-list{display:block!important}.typeahead-container .typeahead-dropdown,.typeahead-container .typeahead-hint,.typeahead-container .typeahead-list,.typeahead-container+.typeahead-backdrop{display:none!important}.typeahead-dropdown .divider{height:1px;margin:5px 0;overflow:hidden;background-color:#e5e5e5}.typeahead-caret{display:inline-block;width:0;height:0;margin-left:2px;vertical-align:middle;border-top:4px solid;border-right:4px solid transparent;border-left:4px solid transparent}.typeahead-search-icon{min-width:40px;height:18px;font-size:13px;display:block;background:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABH0lEQVR4nJ3SvyvFYRTH8deVkkJ3UUZJIbJ8bzJjMtyMym6w2Njs/gCDP0AGCyWjxYDF5GdJYpS6xaIUw/d8771dT7qc+vZ8vs95zvuc5zmnlGWZsG6sYBGjsXeNHWzjQ8JKARjCEUZSh3CJeTy3OjoicxF8hwX0oi/0HSZwiK4UYKUpeBoHeMdb6OnwTWI5BVgMvYZaovwa1kMvpQBjoY8TwVp84ylAO/YV62cKcBt65hfAbKwPKcBu6E2UE8Hl8MF+CrCFG/nwnKKKnviqONOYj6NWQDFIg/I+/3ikFnuUX6d+lY4mR4ZVnMvnoIYLbKCCp0h0otG5egXt2HAED+BFPmAP7bYR7jGHV/RjCjr/AICryFzB3n8ARSX3xc83qRk4q9rDNWcAAAAASUVORK5CYII=) center center no-repeat}

.select2-container .select2-selection--single{
height: 34px;
}

.select2-container .select2-selection--single .select2-selection__rendered{
padding-top: 2px;
}

.dataTables_filter {
     display: none;
}

#classTable .editEnabled .editable{
	display:block;
}

#classTable .editEnabled .default{
	display:none;
}

#classTable .editable{
	display:none;
}

#classTable .default{
	display:show;
}
.morris-hover{position:absolute;z-index:1000}.morris-hover.morris-default-style{border-radius:10px;padding:6px;color:#666;background:rgba(255,255,255,0.8);border:solid 2px rgba(230,230,230,0.8);font-family:sans-serif;font-size:12px;text-align:center}.morris-hover.morris-default-style .morris-hover-row-label{font-weight:bold;margin:0.25em 0}
.morris-hover.morris-default-style .morris-hover-point{white-space:nowrap;margin:0.1em 0}

</style>
 <script src="js/jquery.typeahead.min.js"></script>
<script type="text/javascript" src="js/ManageStudent.js"></script>
 <%List list = (List)request.getSession().getAttribute(Constants.STUDENT_LIST); %>
<script>
/*!
 * jQuery Typeahead
 * Copyright (C) 2015 RunningCoder.org
 * Licensed under the MIT license
 *
 * @author Tom Bertrand
 * @version 2.3.0 (2015-12-25)
 * @link http://www.runningcoder.org/jquerytypeahead/
*/
!function(a){"function"==typeof define&&define.amd?define(["jquery"],function(b){a(window,document,b)}):"object"==typeof exports?module.exports=a(window,document,require("jquery")):a(window,document,window.jQuery)}(function(a,b,c,d){a.Typeahead={version:"2.3.0"};var e={input:null,minLength:2,maxItem:8,dynamic:!1,delay:300,order:null,offset:!1,hint:!1,accent:!1,highlight:!0,group:!1,groupOrder:null,maxItemPerGroup:null,dropdownFilter:!1,dynamicFilter:null,backdrop:!1,backdropOnFocus:!1,cache:!1,ttl:36e5,compression:!1,suggestion:!1,searchOnFocus:!1,resultContainer:null,generateOnLoad:null,mustSelectItem:!1,href:null,display:["display"],template:null,correlativeTemplate:!1,emptyTemplate:!1,filter:!0,matcher:null,source:null,callback:{onInit:null,onReady:null,onShowLayout:null,onHideLayout:null,onSearch:null,onResult:null,onLayoutBuiltBefore:null,onLayoutBuiltAfter:null,onNavigateBefore:null,onNavigateAfter:null,onMouseEnter:null,onMouseLeave:null,onClickBefore:null,onClickAfter:null,onSendRequest:null,onReceiveRequest:null,onSubmit:null},selector:{container:"typeahead-container",result:"typeahead-result",list:"typeahead-list",group:"typeahead-group",item:"typeahead-item",empty:"typeahead-empty",display:"typeahead-display",query:"typeahead-query",filter:"typeahead-filter",filterButton:"typeahead-filter-button",filterValue:"typeahead-filter-value",dropdown:"typeahead-dropdown",dropdownCaret:"typeahead-caret",button:"typeahead-button",backdrop:"typeahead-backdrop",hint:"typeahead-hint"},debug:!1},f=".typeahead",g={from:"ãàáäâẽèéëêìíïîõòóöôùúüûñç",to:"aaaaaeeeeeiiiiooooouuuunc"},h=~navigator.appVersion.indexOf("MSIE 9."),i=function(a,b){this.rawQuery="",this.query="",this.tmpSource={},this.source={},this.isGenerated=null,this.generatedGroupCount=0,this.groupCount=0,this.groupBy="group",this.result={},this.resultCount=0,this.options=b,this.node=a,this.container=null,this.resultContainer=null,this.item=null,this.xhr={},this.hintIndex=null,this.filters={dropdown:{},dynamic:{}},this.requests={},this.backdrop={},this.hint={},this.__construct()};i.prototype={extendOptions:function(){this.options.dynamic&&(this.options.cache=!1,this.options.compression=!1),this.options.cache&&(this.options.cache=function(b){var c,d=["localStorage","sessionStorage"];if(b===!0)b="localStorage";else if("string"==typeof b&&!~d.indexOf(b))return!1;c="undefined"!=typeof a[b];try{a[b].setItem("typeahead","typeahead"),a[b].removeItem("typeahead")}catch(e){c=!1}return c&&b||!1}.call(this,this.options.cache)),this.options.compression&&("object"==typeof LZString&&this.options.cache||(this.options.compression=!1)),"undefined"==typeof this.options.maxItem||/^\d+$/.test(this.options.maxItem)&&0!==this.options.maxItem||(this.options.maxItem=1/0),this.options.maxItemPerGroup&&!/^\d+$/.test(this.options.maxItemPerGroup)&&(this.options.maxItemPerGroup=null),!this.options.display||this.options.display instanceof Array||(this.options.display=[this.options.display]),!this.options.group||this.options.group instanceof Array||(this.options.group=[this.options.group]),this.options.highlight&&!~["any",!0].indexOf(this.options.highlight)&&(this.options.highlight=!1),!this.options.dynamicFilter||this.options.dynamicFilter instanceof Array||(this.options.dynamicFilter=[this.options.dynamicFilter]),this.options.accent&&("object"==typeof this.options.accent?this.options.accent.from&&this.options.accent.to&&this.options.accent.from.length===this.options.accent.to.length:this.options.accent=g),this.options.resultContainer&&("string"==typeof this.options.resultContainer&&(this.options.resultContainer=c(this.options.resultContainer)),this.options.resultContainer instanceof jQuery&&this.options.resultContainer[0]&&(this.resultContainer=this.options.resultContainer)),this.options.group&&"string"==typeof this.options.group[0]&&this.options.maxItemPerGroup&&(this.groupBy=this.options.group[0]),this.options.callback&&this.options.callback.onClick&&(this.options.callback.onClickBefore=this.options.callback.onClick,delete this.options.callback.onClick),this.options.callback&&this.options.callback.onNavigate&&(this.options.callback.onNavigateBefore=this.options.callback.onNavigate,delete this.options.callback.onNavigate),this.options=c.extend(!0,{},e,this.options)},unifySourceFormat:function(){if(this.options.source instanceof Array)return this.options.source={group:{data:this.options.source}},this.groupCount+=1,!0;("undefined"!=typeof this.options.source.data||"undefined"!=typeof this.options.source.url)&&(this.options.source={group:this.options.source});var a;for(var b in this.options.source)if(this.options.source.hasOwnProperty(b)){if(a=this.options.source[b],("string"==typeof a||a instanceof Array)&&(a={url:a}),!a.data&&!a.url)return!1;!a.display||a.display instanceof Array||(a.display=[a.display]),a.ignore&&(a.ignore instanceof RegExp||delete a.ignore),this.options.source[b]=a,this.groupCount+=1}return!0},init:function(){this.helper.executeCallback.call(this,this.options.callback.onInit,[this.node]),this.container=this.node.closest("."+this.options.selector.container)},delegateEvents:function(){var a=this,b=["focus"+f,"input"+f,"propertychange"+f,"keydown"+f,"keyup"+f,"dynamic"+f,"generateOnLoad"+f];this.container.off(f).on("click"+f+" touchstart"+f,function(b){b.stopPropagation(),a.options.dropdownFilter&&a.container.hasClass("filter")&&!c(b.target).closest("."+a.options.selector.dropdown.replace(" ","."))[0]&&a.container.removeClass("filter")}),this.node.closest("form").on("submit",function(b){return a.options.mustSelectItem&&a.helper.isEmpty(a.item)?void b.preventDefault():(a.hideLayout(),a.rawQuery="",a.query="",a.helper.executeCallback.call(a,a.options.callback.onSubmit,[a.node,this,a.item,b])?!1:void 0)});var d=!1;this.node.off(f).on(b.join(" "),function(b){switch(b.type){case"focus":a.options.backdropOnFocus&&(a.buildBackdropLayout(),a.showLayout());case"generateOnLoad":a.isGenerated&&a.options.searchOnFocus&&a.query.length>=a.options.minLength&&a.showLayout(),null!==a.isGenerated||a.options.dynamic||a.generateSource();break;case"keydown":b.keyCode&&~[9,13,27,38,39,40].indexOf(b.keyCode)&&(d=!0,a.navigate(b));break;case"keyup":h&&a.node[0].value.replace(/^\s+/,"").toString().length<a.query.length&&a.node.trigger("input"+f);break;case"propertychange":if(d){d=!1;break}case"input":if(a.rawQuery=a.node[0].value.toString(),a.query=a.rawQuery.replace(/^\s+/,""),a.options.hint&&a.hint.container&&""!==a.hint.container.val()&&0!==a.hint.container.val().indexOf(a.rawQuery)&&a.hint.container.val(""),a.options.dynamic)return a.isGenerated=null,void a.helper.typeWatch(function(){a.query.length>=a.options.minLength?a.generateSource():a.hideLayout()},a.options.delay);case"dynamic":if(!a.isGenerated)break;if(a.query.length<a.options.minLength){a.resetLayout(),a.hideLayout();break}a.searchResult(),a.buildLayout(),a.result.length>0||a.options.emptyTemplate?a.showLayout():a.hideLayout()}}),this.options.generateOnLoad&&this.node.trigger("generateOnLoad"+f)},generateSource:function(){if(!this.isGenerated||this.options.dynamic){if(this.generatedGroupCount=0,this.isGenerated=!1,!this.helper.isEmpty(this.xhr)){for(var b in this.xhr)this.xhr.hasOwnProperty(b)&&this.xhr[b].abort();this.xhr={}}var c,d,e,f;for(c in this.options.source)if(this.options.source.hasOwnProperty(c)){if(d=this.options.source[c],this.options.cache&&(e=a[this.options.cache].getItem(this.node.selector+":"+c))){this.options.compression&&(e=LZString.decompressFromUTF16(e)),f=!1;try{e=JSON.parse(e+""),e.data&&e.ttl>(new Date).getTime()?(this.populateSource(e.data,c),f=!0):a[this.options.cache].removeItem(this.node.selector+":"+c)}catch(g){}if(f)continue}!d.data||d.url?d.url&&(this.requests[c]||(this.requests[c]=this.generateRequestObject(c))):this.populateSource("function"==typeof d.data&&d.data()||d.data,c)}this.handleRequests()}},generateRequestObject:function(a){var b=this,d=this.options.source[a];d.url instanceof Array||(d.url=[d.url]);var e={request:{url:null,dataType:"json",beforeSend:function(c,e){b.xhr[a]=c;var f=b.requests[a].extra.beforeSend||d.url[0].beforeSend;"function"==typeof f&&f.apply(null,arguments)}},extra:{path:null,group:a,callback:{done:null,fail:null,complete:null,always:null}},validForGroup:[a]};Object.defineProperty(e.request,"beforeSend",{writable:!1}),d.url[0]instanceof Object?(d.url[0].callback&&(e.extra.callback=d.url[0].callback,delete d.url[0].callback),e.request=c.extend(!0,e.request,d.url[0])):"string"==typeof d.url[0]&&(e.request.url=d.url[0]),d.url[1]&&"string"==typeof d.url[1]&&(e.extra.path=d.url[1]),"jsonp"===e.request.dataType.toLowerCase()&&(e.request.jsonpCallback="callback_"+a);var f;for(var g in this.requests)if(this.requests.hasOwnProperty(g)&&(f=JSON.stringify(this.requests[g].request),f===JSON.stringify(e.request))){this.requests[g].validForGroup.push(a),e.isDuplicated=!0,delete e.validForGroup;break}return e},handleRequests:function(){var a=this,b=Object.keys(this.requests).length;this.helper.executeCallback.call(this,this.options.callback.onSendRequest,[this.node,this.query]);for(var d in this.requests)this.requests.hasOwnProperty(d)&&(this.requests[d].isDuplicated||!function(d,e){if("function"==typeof a.options.source[d].url[0]){var f=a.options.source[d].url[0].call(a,a.query);if(e.request=c.extend(!0,e.request,f),"object"!=typeof e.request||!e.request.url)return;f.beforeSend&&(a.requests[d].extra.beforeSend=f.beforeSend)}var g,h=!1;if(~e.request.url.indexOf("{{query}}")&&(h||(e=c.extend(!0,{},e),h=!0),e.request.url=e.request.url.replace("{{query}}",a.query.sanitize())),e.request.data)for(var i in e.request.data)if(e.request.data.hasOwnProperty(i)&&~String(e.request.data[i]).indexOf("{{query}}")){h||(e=c.extend(!0,{},e),h=!0),e.request.data[i]=e.request.data[i].replace("{{query}}",a.query.sanitize());break}c.ajax(e.request).done(function(c,d,f){for(var h,i=0;i<e.validForGroup.length;i++)g=a.requests[e.validForGroup[i]],g.extra.callback.done instanceof Function&&(h=g.extra.callback.done(c,d,f),c=h instanceof Array&&h||c),a.populateSource(c,g.extra.group,g.extra.path),b-=1,0===b&&a.helper.executeCallback.call(a,a.options.callback.onReceiveRequest,[a.node,a.query])}).fail(function(b,c,d){for(var f=0;f<e.validForGroup.length;f++)g=a.requests[e.validForGroup[f]],g.extra.callback.fail instanceof Function&&g.extra.callback.fail(b,c,d)}).then(function(b,c){for(var d=0;d<e.validForGroup.length;d++)g=a.requests[e.validForGroup[d]],g.extra.callback.then instanceof Function&&g.extra.callback.then(b,c)}).always(function(b,c,d){for(var f=0;f<e.validForGroup.length;f++)g=a.requests[e.validForGroup[f]],g.extra.callback.always instanceof Function&&g.extra.callback.always(b,c,d)})}(d,this.requests[d]))},populateSource:function(b,c,d){var e=this,f=this.options.source[c],g=f.url&&f.data;b="string"==typeof d?this.helper.namespace(d,b):b,b instanceof Array||(b=[]),g&&("function"==typeof g&&(g=g()),g instanceof Array&&(b=b.concat(g)));for(var h,i=f.display?"compiled"===f.display[0]?f.display[1]:f.display[0]:"compiled"===this.options.display[0]?this.options.display[1]:this.options.display[0],j=0;j<b.length;j++)"string"==typeof b[j]&&(h={},h[i]=b[j],b[j]=h),b[j].group=c;if(this.options.correlativeTemplate){var k=f.template||this.options.template,l="";if(k){if(this.options.correlativeTemplate instanceof Array)for(var j=0;j<this.options.correlativeTemplate.length;j++)l+="{{"+this.options.correlativeTemplate[j]+"}} ";else l=k.replace(/<.+?>/g,"");for(var j=0;j<b.length;j++)b[j].compiled=l.replace(/\{\{([\w\-\.]+)(?:\|(\w+))?}}/g,function(a,c){return e.helper.namespace(c,b[j],"get","")}).trim();f.display?~f.display.indexOf("compiled")||f.display.unshift("compiled"):~this.options.display.indexOf("compiled")||this.options.display.unshift("compiled")}else;}if(this.tmpSource[c]=b,this.options.cache&&!a[this.options.cache].getItem(this.node.selector+":"+c)){var m=JSON.stringify({data:b,ttl:(new Date).getTime()+this.options.ttl});this.options.compression&&(m=LZString.compressToUTF16(m)),a[this.options.cache].setItem(this.node.selector+":"+c,m)}this.incrementGeneratedGroup()},incrementGeneratedGroup:function(){if(this.generatedGroupCount+=1,this.groupCount===this.generatedGroupCount){this.isGenerated=!0,this.xhr={};for(var a=Object.keys(this.options.source),b=0;b<a.length;b++)this.source[a[b]]=this.tmpSource[a[b]];this.tmpSource={},this.node.trigger("dynamic"+f)}},navigate:function(a){if(this.helper.executeCallback.call(this,this.options.callback.onNavigateBefore,[this.node,this.query,a]),~[9,27].indexOf(a.keyCode))return this.query.length||27!==a.keyCode||this.node.blur(),void this.hideLayout();if(this.isGenerated&&this.result.length){var b=this.resultContainer.find("> ul > li:not([data-search-group])"),c=b.filter(".active"),d=c[0]&&b.index(c)||null,e=null;if(13===a.keyCode){if(c.length>0)return a.preventDefault(),a.stopPropagation(),void c.find("a:first").trigger("click");if(this.options.mustSelectItem&&this.helper.isEmpty(this.item))return;return void this.hideLayout()}if(39===a.keyCode)return void(d?b.eq(d).find("a:first")[0].click():this.options.hint&&""!==this.hint.container.val()&&this.helper.getCaret(this.node[0])>=this.query.length&&b.find('a[data-index="'+this.hintIndex+'"]')[0].click());b.length>0&&c.removeClass("active"),38===a.keyCode?(a.preventDefault(),c.length>0?d-1>=0&&(e=d-1,b.eq(e).addClass("active")):(e=b.length-1,b.last().addClass("active"))):40===a.keyCode&&(a.preventDefault(),c.length>0?d+1<b.length&&(e=d+1,b.eq(e).addClass("active")):(e=0,b.first().addClass("active"))),a.preventInputChange&&~[38,40].indexOf(a.keyCode)&&this.buildHintLayout(null!==e&&e<this.result.length?[this.result[e]]:null),this.options.hint&&this.hint.container&&this.hint.container.css("color",a.preventInputChange?this.hint.css.color:null===e&&this.hint.css.color||this.hint.container.css("background-color")||"fff"),this.node.val(null===e||a.preventInputChange?this.rawQuery:this.result[e][this.result[e].matchedKey]),this.helper.executeCallback.call(this,this.options.callback.onNavigateAfter,[this.node,this.query,a])}},searchResult:function(a){a||(this.item={}),this.helper.executeCallback.call(this,this.options.callback.onSearch,[this.node,this.query]),this.result={},this.resultCount=0,this.resultItemCount=0;var b,c,e,f,g,h,i,j,k,l,m,n,o=this,p=this.options.group&&"boolean"!=typeof this.options.group[0]?this.options.group[0]:"group",q=null,r=this.query.toLowerCase(),s=this.options.maxItemPerGroup,t=this.filters.dynamic&&!this.helper.isEmpty(this.filters.dynamic),u="function"==typeof this.options.matcher&&this.options.matcher;this.options.accent&&(r=this.helper.removeAccent.call(this,r));for(b in this.source)if(this.source.hasOwnProperty(b)&&(!this.filters.dropdown||"group"!==this.filters.dropdown.key||this.filters.dropdown.value===b)){h="undefined"!=typeof this.options.source[b].filter?this.options.source[b].filter:this.options.filter,j="function"==typeof this.options.source[b].matcher&&this.options.source[b].matcher||u;for(var v=0;v<this.source[b].length&&(!(this.result.length>=this.options.maxItem)||this.options.callback.onResult);v++)if(!t||this.dynamicFilter.validate.apply(this,[this.source[b][v]])){if(c=this.source[b][v],q="group"===p?b:c[p],q&&!this.result[q]&&(this.result[q]=[]),s&&"group"===p&&this.result[q].length>=s&&!this.options.callback.onResult)break;g=this.options.source[b].display||this.options.display;for(var w=0;w<g.length;w++){if("function"==typeof h){if(i=h.call(this,c,c[g[w]]),i===d)break;if(!i)continue;"object"==typeof i&&(c=i)}if(~[d,!0].indexOf(h)){if(f=c[g[w]],!f)continue;if(f=f.toString().toLowerCase(),this.options.accent&&(f=this.helper.removeAccent.call(this,f)),e=f.indexOf(r),this.options.correlativeTemplate&&"compiled"===g[w]&&0>e&&/\s/.test(r)){l=!0,m=r.split(" "),n=f;for(var x=0;x<m.length;x++)if(""!==m[x]){if(!~n.indexOf(m[x])){l=!1;break}n=n.replace(m[x],"")}}if(0>e&&!l)continue;if(this.options.offset&&0!==e)continue;if(this.options.source[b].ignore&&this.options.source[b].ignore.test(f))continue;if(j){if(k=j.call(this,c,c[g[w]]),k===d)break;if(!k)continue;"object"==typeof k&&(c=k)}}if(!this.filters.dropdown||this.filters.dropdown.value==c[this.filters.dropdown.key]){if(this.resultCount++,this.resultItemCount<this.options.maxItem){if(s&&this.result[q].length>=s)break;c.matchedKey=g[w],this.result[q].push(c),this.resultItemCount++}break}}if(!this.options.callback.onResult){if(this.resultItemCount>=this.options.maxItem)break;if(s&&this.result[q].length>=s&&"group"===p)break}}}if(this.options.order){var y,g=[];for(var b in this.result)if(this.result.hasOwnProperty(b)){for(var w=0;w<this.result[b].length;w++)y=this.options.source[this.result[b][w].group].display||this.options.display,~g.indexOf(y[0])||g.push(y[0]);this.result[b].sort(o.helper.sort(g,"asc"===o.options.order,function(a){return a.toString().toUpperCase()}))}}var z,A=[];z="function"==typeof this.options.groupOrder?this.options.groupOrder.apply(this,[this.node,this.query,this.result,this.resultCount]):this.options.groupOrder instanceof Array?this.options.groupOrder:"string"==typeof this.options.groupOrder&&~["asc","desc"].indexOf(this.options.groupOrder)?Object.keys(this.result).sort(o.helper.sort([],"asc"===o.options.groupOrder,function(a){return a.toString().toUpperCase()})):Object.keys(this.result);for(var w=0;w<z.length;w++)A=A.concat(this.result[z[w]]||[]);this.result=A,this.helper.executeCallback.call(this,this.options.callback.onResult,[this.node,this.query,this.result,this.resultCount])},buildLayout:function(){this.resultContainer||(this.resultContainer=c("<div/>",{"class":this.options.selector.result}),this.container.append(this.resultContainer));var a=this.query.toLowerCase();this.options.accent&&(a=this.helper.removeAccent.call(this,a));var b=this,d=c("<ul/>",{"class":this.options.selector.list+(b.helper.isEmpty(b.result)?" empty":""),html:function(){if(b.options.emptyTemplate&&b.helper.isEmpty(b.result)){var d="function"==typeof b.options.emptyTemplate?b.options.emptyTemplate.call(b,b.query):b.options.emptyTemplate.replace(/\{\{query}}/gi,b.query.sanitize());return d instanceof jQuery&&"LI"===d[0].nodeName?d:c("<li/>",{"class":b.options.selector.empty,html:c("<a/>",{href:"javascript:;",html:d})})}for(var e in b.result)b.result.hasOwnProperty(e)&&!function(d,e,f){var g,h,i,j,k=e.group,l=[],m=b.options.source[e.group].display||b.options.display,n=b.options.source[e.group].href||b.options.href;b.options.group&&(b.options.group[1]?"function"==typeof b.options.group[1]?k=b.options.group[1](e):"string"==typeof b.options.group[1]&&(k=b.options.group[1].replace(/(\{\{group}})/gi,e[b.options.group[0]]||k)):"boolean"!=typeof b.options.group[0]&&e[b.options.group[0]]&&(k=e[b.options.group[0]]),c(f).find('li[data-search-group="'+k+'"]')[0]||c(f).append(c("<li/>",{"class":b.options.selector.group,html:c("<a/>",{href:"javascript:;",html:k}),"data-search-group":k}))),g=c("<li/>",{"class":b.options.selector.item,html:c("<a/>",{href:function(){return n&&("string"==typeof n?n=n.replace(/\{\{([\w\-\.]+)(?:\|(\w+))?}}/g,function(a,c,d){var f=b.helper.namespace(c,e,"get","");return d&&"raw"===d?f:b.helper.slugify.call(b,f)}):"function"==typeof n&&(n=n(e)),e.href=n),n||"javascript:;"},"data-group":k,"data-index":d,html:function(){if(j=e.group&&b.options.source[e.group].template||b.options.template)"function"==typeof j&&(j=j.call(b,b.query,e)),h=j.replace(/\{\{([\w\-\.]+)(?:\|(\w+))?}}/g,function(c,d,f){var g=b.helper.namespace(d,e,"get","");return f&&"raw"===f||b.options.highlight===!0&&a&&~m.indexOf(d)&&(g=b.helper.highlight.call(b,g,a.split(" "),b.options.accent)),g});else{for(var d=0;d<m.length;d++)l.push(e[m[d]]);h='<span class="'+b.options.selector.display+'">'+l.join(" ")+"</span>"}(b.options.highlight===!0&&a&&!j||"any"===b.options.highlight)&&(h=b.helper.highlight.call(b,h,a.split(" "),b.options.accent)),c(this).append(h)},click:function(a){return b.options.mustSelectItem&&b.helper.isEmpty(e)?void a.preventDefault():(b.item=e,b.helper.executeCallback.call(b,b.options.callback.onClickBefore,[b.node,this,e,a]),void(a.originalEvent&&a.originalEvent.defaultPrevented||a.isDefaultPrevented()||(b.query=b.rawQuery=e[e.matchedKey].toString(),b.node.val(b.query).focus(),b.searchResult(!0),b.buildLayout(),b.hideLayout(),b.helper.executeCallback.call(b,b.options.callback.onClickAfter,[b.node,this,e,a]))))},mouseenter:function(a){c(this).closest("ul").find("li.active").removeClass("active"),c(this).closest("li").addClass("active"),b.helper.executeCallback.call(b,b.options.callback.onMouseEnter,[b.node,this,e,a])},mouseleave:function(a){c(this).closest("li").removeClass("active"),b.helper.executeCallback.call(b,b.options.callback.onMouseLeave,[b.node,this,e,a])}})}),b.options.group?(i=c(f).find('a[data-group="'+k+'"]:last').closest("li"),i[0]||(i=c(f).find('li[data-search-group="'+k+'"]')),c(g).insertAfter(i)):c(f).append(g)}(e,b.result[e],this)}});if(this.buildBackdropLayout(),this.buildHintLayout(),this.options.callback.onLayoutBuiltBefore){var e=this.helper.executeCallback.call(this,this.options.callback.onLayoutBuiltBefore,[this.node,this.query,this.result,d]);e instanceof jQuery&&(d=e)}this.resultContainer.html(d),this.options.callback.onLayoutBuiltAfter&&this.helper.executeCallback.call(this,this.options.callback.onLayoutBuiltAfter,[this.node,this.query,this.result])},buildBackdropLayout:function(){this.options.backdrop&&(this.backdrop.container||(this.backdrop.css=c.extend({opacity:.6,filter:"alpha(opacity=60)",position:"fixed",top:0,right:0,bottom:0,left:0,"z-index":1040,"background-color":"#000"},this.options.backdrop),this.backdrop.container=c("<div/>",{"class":this.options.selector.backdrop,css:this.backdrop.css}).insertAfter(this.container)),this.container.addClass("backdrop").css({"z-index":this.backdrop.css["z-index"]+1,position:"relative"}))},buildHintLayout:function(a){if(this.options.hint){var b=this,d="",a=a||this.result,e=this.query.toLowerCase();if(this.options.accent&&(e=this.helper.removeAccent.call(this,e)),this.hintIndex=null,this.query.length>=this.options.minLength){if(this.hint.container||(this.hint.css=c.extend({"border-color":"transparent",position:"absolute",top:0,display:"inline","z-index":-1,"float":"none",color:"silver","box-shadow":"none",cursor:"default","-webkit-user-select":"none","-moz-user-select":"none","-ms-user-select":"none","user-select":"none"},this.options.hint),this.hint.container=c("<input/>",{type:this.node.attr("type"),"class":this.node.attr("class"),readonly:!0,unselectable:"on",tabindex:-1,click:function(){b.node.focus()}}).addClass(this.options.selector.hint).css(this.hint.css).insertAfter(this.node),this.node.parent().css({position:"relative"})),this.hint.container.css("color",this.hint.css.color),e)for(var f,g,h,i=0;i<a.length;i++){g=a[i].group,f=this.options.source[g].display||this.options.display;for(var j=0;j<f.length;j++)if(h=String(a[i][f[j]]).toLowerCase(),this.options.accent&&(h=this.helper.removeAccent.call(this,h)),0===h.indexOf(e)){d=String(a[i][f[j]]),this.hintIndex=i;break}if(null!==this.hintIndex)break}this.hint.container.val(d.length>0&&this.rawQuery+d.substring(this.query.length)||"")}}},buildDropdownLayout:function(){function a(a){"*"===a.value?delete this.filters.dropdown:this.filters.dropdown=a,this.container.removeClass("filter").find("."+this.options.selector.filterValue).html(a.display||a.value),this.node.trigger("dynamic"+f),this.node.focus()}if(this.options.dropdownFilter){var b,d=this;if("boolean"==typeof this.options.dropdownFilter)b="all";else if("string"==typeof this.options.dropdownFilter)b=this.options.dropdownFilter;else if(this.options.dropdownFilter instanceof Array)for(var e=0;e<this.options.dropdownFilter.length;e++)if("*"===this.options.dropdownFilter[e].value&&this.options.dropdownFilter[e].display){b=this.options.dropdownFilter[e].display;break}c("<span/>",{"class":this.options.selector.filter,html:function(){c(this).append(c("<button/>",{type:"button","class":d.options.selector.filterButton,html:"<span class='"+d.options.selector.filterValue+"'>"+b+"</span> <span class='"+d.options.selector.dropdownCaret+"'></span>",click:function(a){a.stopPropagation(),d.container.toggleClass("filter")}})),c(this).append(c("<ul/>",{"class":d.options.selector.dropdown,html:function(){var b=d.options.dropdownFilter;if(~["string","boolean"].indexOf(typeof d.options.dropdownFilter)){b=[];for(var e in d.options.source)d.options.source.hasOwnProperty(e)&&b.push({key:"group",value:e});b.push({key:"group",value:"*",display:"string"==typeof d.options.dropdownFilter&&d.options.dropdownFilter||"All"})}for(var f=0;f<b.length;f++)!function(b,e,f){(e.key||"*"===e.value)&&e.value&&("*"===e.value&&c(f).append(c("<li/>",{"class":"divider"})),c(f).append(c("<li/>",{html:c("<a/>",{href:"javascript:;",html:e.display||e.value,click:function(b){b.preventDefault(),a.apply(d,[e])}})})))}(f,b[f],this)}}))}}).insertAfter(d.container.find("."+d.options.selector.query))}},dynamicFilter:{validate:function(a){var b,c,d=null,e=null;for(var f in this.filters.dynamic)if(this.filters.dynamic.hasOwnProperty(f)&&(c=~f.indexOf(".")?this.helper.namespace(f,a,"get"):a[f],"|"!==this.filters.dynamic[f].modifier||d||(d=c==this.filters.dynamic[f].value||!1),"&"===this.filters.dynamic[f].modifier)){if(c!=this.filters.dynamic[f].value){e=!1;break}e=!0}return b=d,null!==e&&(b=e,e===!0&&null!==d&&(b=d)),!!b},set:function(a,b){var c=a.match(/^([|&])?(.+)/);b?this.filters.dynamic[c[2]]={modifier:c[1]||"|",value:b}:delete this.filters.dynamic[c[2]],this.searchResult(),this.buildLayout()},bind:function(){if(this.options.dynamicFilter)for(var a,b=this,d=0;d<this.options.dynamicFilter.length;d++)a=this.options.dynamicFilter[d],"string"==typeof a.selector&&(a.selector=c(a.selector)),a.selector instanceof jQuery&&a.selector[0]&&a.key&&!function(a){a.selector.off(f).on("change"+f,function(){b.dynamicFilter.set.apply(b,[a.key,b.dynamicFilter.getValue(this)])}).trigger("change"+f)}(a)},getValue:function(a){var b;return"SELECT"===a.tagName?b=a.value:"INPUT"===a.tagName&&("checkbox"===a.type?b=a.checked||null:"radio"===a.type&&a.checked&&(b=a.value)),b}},showLayout:function(){if(!this.container.hasClass("result")&&(this.result.length||this.options.emptyTemplate||this.options.backdropOnFocus)){var a=this;c("html").off(f).one("click"+f+" touchstart"+f,function(){a.hideLayout()}),this.container.addClass("result hint backdrop"),this.helper.executeCallback.call(this,this.options.callback.onShowLayout,[this.node,this.query])}},hideLayout:function(){this.container.removeClass("result hint filter"+(this.options.backdropOnFocus&&c(this.node).is(":focus")?"":" backdrop")),this.options.backdropOnFocus&&this.container.hasClass("backdrop")||(c("html").off(f),this.helper.executeCallback.call(this,this.options.callback.onHideLayout,[this.node,this.query]))},resetLayout:function(){this.result={},this.resultCount=0,this.resultItemCount=0,this.resultContainer&&this.resultContainer.html(""),this.options.hint&&this.hint.container&&this.hint.container.val("")},__construct:function(){this.extendOptions(),this.unifySourceFormat()&&(this.init(),this.delegateEvents(),this.buildDropdownLayout(),this.dynamicFilter.bind.apply(this),this.helper.executeCallback.call(this,this.options.callback.onReady,[this.node]))},helper:{isEmpty:function(a){for(var b in a)if(a.hasOwnProperty(b))return!1;return!0},removeAccent:function(a){if("string"==typeof a){var b=this.options.accent||g;return a=a.toLowerCase().replace(new RegExp("["+b.from+"]","g"),function(a){return b.to[b.from.indexOf(a)]})}},slugify:function(a){return a=String(a),""!==a&&(a=this.helper.removeAccent.call(this,a),a=a.replace(/[^-a-z0-9]+/g,"-").replace(/-+/g,"-").trim("-")),a},sort:function(a,b,c){var d=function(b){for(var d=0;d<a.length;d++)if("undefined"!=typeof b[a[d]])return c(b[a[d]]);return b};return b=[-1,1][+!!b],function(a,c){return a=d(a),c=d(c),b*((a>c)-(c>a))}},replaceAt:function(a,b,c,d){return a.substring(0,b)+d+a.substring(b+c)},highlight:function(a,b,c){a=String(a);var d=c&&this.helper.removeAccent.call(this,a)||a,e=[];b instanceof Array||(b=[b]),b.sort(function(a,b){return b.length-a.length});for(var f=b.length-1;f>=0;f--)""!==b[f].trim()?b[f]=b[f].replace(/[-[\]{}()*+?.,\\^$|#\s]/g,"\\$&"):b.splice(f,1);d.replace(new RegExp("(?:"+b.join("|")+")(?!([^<]+)?>)","gi"),function(a,b,c){e.push({offset:c,length:a.length})});for(var f=e.length-1;f>=0;f--)a=this.helper.replaceAt(a,e[f].offset,e[f].length,"<strong>"+a.substr(e[f].offset,e[f].length)+"</strong>");return a},getCaret:function(a){if(a.selectionStart)return a.selectionStart;if(b.selection){a.focus();var c=b.selection.createRange();if(null==c)return 0;var d=a.createTextRange(),e=d.duplicate();return d.moveToBookmark(c.getBookmark()),e.setEndPoint("EndToStart",d),e.text.length}return 0},executeCallback:function(b,c){if(!b)return!1;var d;if("function"==typeof b)d=b;else if(("string"==typeof b||b instanceof Array)&&("string"==typeof b&&(b=[b,[]]),d=this.helper.namespace(b[0],a),"function"!=typeof d))return!1;return d.apply(this,(b[1]||[]).concat(c?c:[]))||!0},namespace:function(b,c,e,f){if("string"!=typeof b||""===b)return!1;for(var g=b.split("."),h=c||a,e=e||"get",i=f||{},j="",k=0,l=g.length;l>k;k++){if(j=g[k],"undefined"==typeof h[j]){if(~["get","delete"].indexOf(e))return"undefined"!=typeof f?f:d;h[j]={}}if(~["set","create","delete"].indexOf(e)&&k===l-1){if("set"!==e&&"create"!==e)return delete h[j],!0;h[j]=i}h=h[j]}return h},typeWatch:function(){var a=0;return function(b,c){clearTimeout(a),a=setTimeout(b,c)}}()}},c.fn.typeahead=c.typeahead=function(a){return j.typeahead(this,a)};var j={typeahead:function(b,d){if(d&&d.source&&"object"==typeof d.source){if("function"==typeof b){if(!d.input)return;b=c(d.input)}if(b.length&&"INPUT"===b[0].nodeName)for(var e,f=0;f<b.length;f++)e=1===b.length?b:c(b.selector.split(",")[f].trim()),a.Typeahead[e.selector||d.input]=new i(e,d)}}};"sanitize"in String.prototype||(String.prototype.sanitize=function(){var a={"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;","/":"&#x2F;"};return this.replace(/[&<>"'\/]/g,function(b){return a[b]})}),a.console=a.console||{log:function(){}},"trim"in String.prototype||(String.prototype.trim=function(){return this.replace(/^\s+/,"").replace(/\s+$/,"")}),"indexOf"in Array.prototype||(Array.prototype.indexOf=function(a,b){b===d&&(b=0),0>b&&(b+=this.length),0>b&&(b=0);for(var c=this.length;c>b;b++)if(b in this&&this[b]===a)return b;return-1}),Object.keys||(Object.keys=function(a){var b,c=[];for(b in a)Object.prototype.hasOwnProperty.call(a,b)&&c.push(b);return c})});

var generateRollNoUrl = "rest/classownerservice/setRollNumber/";
var studentId="";
var wayOfAddition="";
var globalBatchID = "";
var globalDivisionID = "";
var graphData = [];
var enabledEdit = false;
	$(document).ready(function(){
		$(".containerData").hide();
		$(this).on("click",".btn-batch-edit",enableEdit)
		.on("click",".btn-cancel",cancelEdit)
		.on("click",".btn-save",saveStudent)
		.on("click",".btn-batch-delete",deleteStudentPrompt)
		.on("click",".btn-student-details",studentDetails)
		.on("change","#batch",onBatchChange)
		.on("click",".generateRollNumber",generateRoll);
		var allNames = getNames();
	        $('#studentNameSearch').typeahead({
	            minLength: 1,
	            order: "asc",
	            hint: true,
	            offset : true,
	            template: "{{display}}",
	            source: allNames,
	            debug: true
	        });
		
	    
			$('#datetimepicker').datetimepicker({
			format : 'YYYY-MM-DD',
			pickTime : false,
			maxDate:moment(((new Date()).getMonth()+1)+'/'+(new Date()).getDate()+'/'+(new Date()).getFullYear())
		});
		
		$("#batches").select2({data:'',placeholder:"type batch name"});
		
		$("#division").change(function(){
			getBatchesOfDivision();
		});
		
		$("#searchStudentByName").click(function(){
			var studentName = $("#studentNameSearch").val();
			$.ajax({
				url:"classOwnerServlet",
				data:{
					methodToCall:"getStudentByName",
					studentName:studentName
				},
				type:"post",
				success:function(data){
					successCallbackclass(data,"name");
				},error:function(){
					}
			});	
			
		});
		
		
		
	$(".searchStudentByBatch").click(function(){
		$("#divisionError").empty();
		$("#batchError").empty();
		var divisionID = $(".searchStudent").find("#division").val();
		var batchID = $(".searchStudent").find("#batch").val();
		var flag = false;
		if(divisionID == "-1"){
			$("#divisionError").html("Select Division!");
			flag=true;
		}
		if(batchID == "-1" || batchID == null){
			$("#batchError").html("Select Batch!");
			flag=true;
		}
		if(flag == false){
			globalBatchID = batchID;
			globalDivisionID = divisionID;
		$.ajax({
			url:"classOwnerServlet",
			data:{
				batchID:batchID,
				methodToCall:"getstudentsrelatedtobatch",
				divisionID:divisionID
			},
			type:"post",
			success:function(data){
				successCallbackclass(data,"batch");
			},error:function(){
				}
		});
		}
		});
	
		$("#back").click(function(){
			$(".studentList").show();
			$(".studentDetailsDiv").hide();
		});
		
		 $("#classTable").on("change",".selectDivision",function(){
			 if(enabledEdit == false){
			var divisionId	 = $(this).val();
			var batchDataArray = [];
			var that = $(this);
			 $.ajax({
				   url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "fetchBatchesForDivision",
						 regId:'',
						 divisionId:divisionId						 
				   		},
				   type:"POST",
				   async:false,
				   success:function(e){
					   that.closest("tr").find(".selectBatch").empty();
					    var data = JSON.parse(e);
					    if(data.status!="error"){
					    var tempData ={};
					    tempData.id = "-1";
					    tempData.text = "Select Batch";
					    batchDataArray.push(tempData);
						var batchData = {};
					    $.each(data.batches,function(key,val){
							var data = {};
							data.id = val.batch_id;
							data.text = val.batch_name;
							batchDataArray.push(data);
							batchData[data.id] = val;
						});
					    that.closest("tr").find(".selectBatch").select2({data:batchDataArray,placeholder:"type batch name"}).data("batchData",batchData);
					    }else{
					    	that.closest("tr").find(".selectBatch").select2({data:"",placeholder:"No batch found"});
					    }
				   	},
				   error:function(e){
					   $('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Error while fetching batches for division');
						$('div#addStudentModal .error').show();
				   }
				   
			});
			}
			 enabledEdit = false;
		}); 
		$('.nav-tabs a[href="#marksTab"]').on('shown.bs.tab', function () {
			
			if(graphData.length > 0){
				for (i =0; i <graphData.length;i++){
					
					if(graphData[i].length>0){
						$("#graphData"+i).empty();
						console.log(JSON.stringify(graphData[i]));
						new Morris.Line({
					        element: 'graphData'+i,
					        data:graphData[i],
					        xkey: 'sub',
					        ykeys: ['marks','avg','topper'],
					        labels: ['Marks','Average','Topper'],
					        parseTime:false,
					        ymax : 100
					      });
					}
				}
			}});
		
		$("#feesTab").on("click",".payFees",function(){
			var batch_id = $(this).prop("id");
			var serviceFees_Transaction = {};
			serviceFees_Transaction.batch_id = batch_id;
			serviceFees_Transaction.student_id = studentId;
			serviceFees_Transaction.amt_paid = $(this).closest(".amtData").find("#amt").val();
			var that = $(this);
			var handlers = {};
			handlers.success=function(){
				$.notify({message: "Fee paid successfuly"},{type: 'success'});
				var amt = that.closest(".amtData").find("#amt").val()
				var updatedfeesPaid =  parseFloat(that.closest(".feesData").find(".feesPaid").html()) + parseFloat(amt) ;
				var updatedfeesDue = that.closest(".feesData").find(".feesDue").html() - amt;
				that.closest(".feesData").find(".feesPaid").html(updatedfeesPaid);
				that.closest(".feesData").find(".feesDue").html(updatedfeesDue); 
			};   
			handlers.error=function(){
				$.notify({message: "Error"},{type: 'danger'});
			};   
			
			rest.post("rest/feesservice/saveStudentBatchFeesTransaction/",handlers,JSON.stringify(serviceFees_Transaction));
		});
		
	});
	
	function createAttenanceTab(data){
		$("#attendanceTab").empty();
		var monthNames = {
				1 : 'Jan',
				2 : 'Feb',
				3 : 'Mar',
				4 : 'Apr',
				5 : 'May',
				6 : 'Jun',
				7 : 'Jul',
				8 : 'Aug',
				9 : 'Sep',
				10: 'Oct',
				11: 'Nov',
				12: 'Dec'
				
		}
		var htmlString = "";
		for(i=0;i<data.batchDataList.length;i++){
			htmlString = htmlString + "<div style='text-decoration:underline;font-weight:bold'>Batch : "+data.batchDataList[i].batch_name+"</div>"
			if(data.batchDataList[i].years != null){
			for(y=0;y<data.batchDataList[i].years.length;y++){
				htmlString = htmlString + "<div>Year : "+data.batchDataList[i].years[y]+"</div>"
		 htmlString = htmlString + "<table class='table table-bordered'><thead><tr><td></td>";
			for(j=0;j<data.batchDataList[i].subjectList.length;j++){
				htmlString = htmlString + "<td>"+data.batchDataList[i].subjectList[j].subjectName+"</td>"
			}
			htmlString = htmlString + "<td>Total Lectures</td><td>Attended Lectures</td><td>Average</td></tr></thead>"
			for(monthCount = 1 ;monthCount<=12 ;monthCount++){
				htmlString = htmlString + "<tr><td>"+monthNames[monthCount]+"</td>";
				var totalLectures = 0 ;
				var attendedLectures = 0;
			for(j=0;j<data.batchDataList[i].subjectList.length;j++){
				var tempTotalLectures = 0 ;
				var tempAttendedLectures = 0;
				for(k=0;k<data.monthWiseTotalLectureList.length;k++){
					if(data.monthWiseTotalLectureList[k].batch_id == data.batchDataList[i].batch_id && 
							data.monthWiseTotalLectureList[k].sub_id == data.batchDataList[i].subjectList[j].subjectId &&
							monthCount == data.monthWiseTotalLectureList[k].month && data.monthWiseTotalLectureList[k].year == data.batchDataList[i].years[y]){
						tempTotalLectures = data.monthWiseTotalLectureList[k].count;
						break;
					}
				}
				for(k=0;k<data.monthWiseAttendanceList.length;k++){
					if(data.monthWiseAttendanceList[k].batch_id == data.batchDataList[i].batch_id && 
							data.monthWiseAttendanceList[k].sub_id == data.batchDataList[i].subjectList[j].subjectId &&
							monthCount == data.monthWiseAttendanceList[k].month && data.monthWiseAttendanceList[k].year == data.batchDataList[i].years[y]){
						if(tempTotalLectures > 0){
						htmlString = htmlString + "<td>"+parseFloat(((data.monthWiseAttendanceList[k].count/tempTotalLectures)*100).toFixed(2))+"%</td>";
						tempAttendedLectures = data.monthWiseAttendanceList[k].count;
						}else{
							htmlString = htmlString + "<td>0%</td>";
						}
						break;
					}
				}
				totalLectures = totalLectures + tempTotalLectures;
				attendedLectures = attendedLectures + tempAttendedLectures;
				}
			htmlString = htmlString + "<td>"+totalLectures+"</td>";
			htmlString = htmlString + "<td>"+attendedLectures+"</td>";
			if(totalLectures > 0){
			htmlString = htmlString + "<td>"+parseFloat(((attendedLectures/totalLectures)*100).toFixed(2))+"%</td>";
			}else{
				htmlString = htmlString + "<td>0%</td>";
			}
				htmlString = htmlString + "</tr>";
			}
			htmlString = htmlString + "</thead></table>";
		}
			}else{
				htmlString = htmlString + "<div class='well'>Attendance Data is not available</div>";
			}
		}
		
		$("#attendanceTab").append(htmlString);
	}
	
	function createFeesTab(data,batches){
		$("#feesTab").empty();
		var htmlString = ""
		for(i=0;i<batches.length;i++){
			htmlString = htmlString + "<div style='text-decoration:underline;font-weight:bold'>Batch : "+batches[i].batch_name+"</div>";
			var flag = false;
			for(j=0;j<data.student_FeesList.length;j++){
				if(data.student_FeesList[j].batch_id == batches[i].batch_id){
					htmlString = htmlString + "<div class='well feesData'>";
					htmlString = htmlString + "<div class='row'><div class='col-md-3'>Batch Fees</div><div class='col-md-1'>:</div><div class='col-md-2'>&#x20B9;"+data.student_FeesList[j].batch_fees+"</div></div>";
					if(data.student_FeesList[j].discount_type == 'Amt'){
					htmlString = htmlString + "<div class='row'><div class='col-md-3'>Discount</div><div class='col-md-1'>:</div><div class='col-md-2'>&#x20B9;"+data.student_FeesList[j].discount+"</div></div>";
					}else{
						htmlString = htmlString + "<div class='row'><div class='col-md-3'>Discount</div><div class='col-md-1'>:</div><div class='col-md-2'>"+data.student_FeesList[j].discount+"%</div></div>";	
					}
					htmlString = htmlString + "<div class='row'><div class='col-md-3'>Final Fees</div><div class='col-md-1'>:</div><div class='col-md-2'>&#x20B9;"+data.student_FeesList[j].final_fees_amt+"</div></div>";
					htmlString = htmlString + "<div class='row'><div class='col-md-3'>Fees Paid</div><div class='col-md-1'>:</div><div class='col-md-2'>&#x20B9;<span class='feesPaid'>"+data.student_FeesList[j].fees_paid+"</span></div></div>";
					htmlString = htmlString + "<div class='row'><div class='col-md-3'>Remaining Fees</div><div class='col-md-1'>:</div><div class='col-md-2'>&#x20B9;<span class='feesDue'>"+data.student_FeesList[j].fees_due+"</span></div></div>";
					htmlString = htmlString + "<div class='row amtData'><div class='col-md-2'><input type='number' min=0 id='amt' value='0' class='form-control'></div><div class='col-md-1'><button class='btn btn-primary btn-sm payFees' id='"+batches[i].batch_id+"'>Pay</button></div></div>";
					htmlString = htmlString + "</div>";
					flag = true;
				} 
			}
			if(flag == false){
				htmlString = htmlString + "<div class='well'>Fees Data is not available.</div>";
			}
		}
		
		$("#feesTab").append(htmlString);
	}
	function createProgressCard(data,studentBatches){
		graphData = [];
		$("#marksTab #marksTabData").empty();
		var htmlString = "";
		if(data.detailMarklist != null){
		if(studentBatches != null){	
	 	for(outer = 0 ; studentBatches.length > outer;outer++){
		var newBatchData = "<div class='container'><b>Batch : "+studentBatches[outer].batch_name+"</b><div class='row'><table class='table table-bordered'><thead><tr><th></th>";
		var subjectFlag = false;
		for(i =0 ; i<data.batchExamDistinctSubjectList.length;i++){
			if(data.batchExamDistinctSubjectList[i].batch_id == studentBatches[outer].batch_id){
			newBatchData = newBatchData + "<th>"+data.batchExamDistinctSubjectList[i].subject_name+"</th>";
			subjectFlag = true;
			}
		}
		if(subjectFlag == true ){
		newBatchData = newBatchData + "<th>Total</th>";
		newBatchData = newBatchData + "<th>Out Of</th>";
		newBatchData = newBatchData + "<th>Percentage</th>";
		newBatchData = newBatchData + "</tr></thead>";
		 newBatchData = newBatchData + "<tbody>";
		 
		for(i =0 ; i<data.batchExamList.length;i++){
			if(data.batchExamList[i].batch_id == studentBatches[outer].batch_id){
				var innerGraphData = [];
			newBatchData = newBatchData + "<tr><td>"+data.batchExamList[i].exam_name+"</td>";
			var total_marks = 0;
					for(k=0;k<data.batchExamDistinctSubjectList.length;k++){
						if(data.batchExamDistinctSubjectList[k].batch_id == studentBatches[outer].batch_id){
						var flag = false;
							for(l=0;l<data.detailMarklist.length;l++){
								if(data.batchExamList[i].exam_id == data.detailMarklist[l].exam_id 
										&& data.batchExamDistinctSubjectList[k].subject_id == data.detailMarklist[l].sub_id && 
										data.detailMarklist[l].batch_id==data.batchExamList[i].batch_id){
									var obj = {};
									if(data.detailMarklist[l].examPresentee == ""){
										obj.marks = 0;
										newBatchData = newBatchData + "<td>-</td>";
									}else if(data.detailMarklist[l].examPresentee == "A"){
										obj.marks = 0;
										newBatchData = newBatchData + "<td>A</td>";
									}else{
									if(data.detailMarklist[l].marks>0){
									obj.marks = parseFloat(((data.detailMarklist[l].marks/data.detailMarklist[l].examSubjectTotalMarks)*100).toFixed(2));
									}else{
										obj.marks = 0;
									}
									newBatchData = newBatchData + "<td>"+data.detailMarklist[l].marks+"</td>";
									total_marks = total_marks + data.detailMarklist[l].marks;
									}
									if(data.detailMarklist[l].avgMarks > 0){
									obj.avg =  parseFloat(((data.detailMarklist[l].avgMarks/data.detailMarklist[l].examSubjectTotalMarks)*100).toFixed(2));;
									}else{
										obj.avg = 0;
									}
									if(data.detailMarklist[l].topperMarks > 0){
									obj.topper =  parseFloat(((data.detailMarklist[l].topperMarks/data.detailMarklist[l].examSubjectTotalMarks)*100).toFixed(2));;
									}else{
										obj.topper = 0;
									}
									obj.sub = data.batchExamDistinctSubjectList[k].subject_name;
									obj.exam_name = "Batch : "+studentBatches[outer].batch_name+" / Exam : "+data.batchExamList[i].exam_name;
									innerGraphData.push(obj);
									flag = true;
									break;
								}
							}
							if(flag == false ){
								newBatchData = newBatchData + "<td>-</td>";
							}
						}
					}
					newBatchData = newBatchData + "<td>"+total_marks+"</td>";
					newBatchData = newBatchData + "<td>"+data.batchExamList[i].marks+"</td>";
					if(data.batchExamList[i].marks > 0){
					newBatchData = newBatchData + "<td>"+parseFloat(((total_marks/data.batchExamList[i].marks)*100).toFixed(2))+"%</td>";
					}else{
						newBatchData = newBatchData + "<td>0%</td>";
					}
					graphData.push(innerGraphData);
			newBatchData = newBatchData + "</tr>";
			}
		} 
		newBatchData = newBatchData +"</tbody></table></div></div>";
		}else{
			newBatchData = "<div class='container'><b>Batch : "+studentBatches[outer].batch_name+"</b><div class='well'>Marks Data is not available.</div></div>"
		}
		htmlString = htmlString + newBatchData;
 }
 }
 	console.log("Graph Data"+graphData);
		//$("#marksTab #marksTabData").append(htmlString);
		}else{
			htmlString = htmlString + "<div class='well'>Marks Data is not available.</div>";
		}
		$("#marksTab #marksTabData").append(htmlString);
	}
	
	function generateRoll(){
		var handler = {};
		handler.success = function(e){$.notify({message: "Roll number generated"},{type: 'success'});};
		handler.error = function(e){$.notify({message: "Error"},{type: 'danger'});};
		rest.post(generateRollNoUrl+$("#division").val()+"/"+$("#batch").val(),handler);
	}
	function onBatchChange(){
		$(".containerData").hide();
		var batchID = $(".searchStudent").find("#batch").val();
		if(batchID!=-1){
			var batchData = $("#batch").data("batchData")[batchID];
			if(!batchData.status || batchData.status.indexOf("rollGenerated=yes")==-1){
				$('.generateRollNumber').removeClass("hide").text("Generate roll number").attr("data-toggle","tooltip").attr("data-original-title","Roll number for this batch are not generated, please generate roll number");
			}else{
				$('.generateRollNumber').removeClass("hide").text("Re Generate roll number").attr("data-toggle","tooltip").attr("data-original-title","Roll number for this batch are already generated, Click to regenerate, this may loose previously generated roll number");
			}
			$('.generateRollNumber').tooltip();
		}else{
			$('.generateRollNumber').addClass('hide');
		}
	}
	
	function studentDetails(){
		$(".studentDetails").find("#batchID").val(globalBatchID);
		$(".studentDetails").find("#divisionID").val(globalDivisionID);
		$(".studentDetails").find("#studentID").val($(this).closest("tr").find("#studentId").val());
		$(".studentDetails").find("#currentPage").val($(".paginate_button current").text());
		 $("#generalTab").find(".crendentialDetails").empty();
		var formdata=$(".studentDetails").serialize();
		 studentId = $(this).closest("tr").find("#studentId").val();
		/* $.ajax({
			 url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "getStudentDetails",
					 regId:'',
					 studentId:studentId
			   		},
			   type:"POST",
			   success:function(e){					  
				   var data = JSON.parse(e);
				   var json = data.studentDetails;
				   $("#studentDetailsBday").html(json.studentUserBean.dob.substring(6)+"/"+json.studentUserBean.dob.substring(4,6)+"/"+json.studentUserBean.dob.substring(0,4));
				   $("#studentDetailsName").html(json.studentUserBean.fname+" "+json.studentUserBean.lname);
				   var batchNames = "";
				   $.each(json.batches,function(key,val){
						batchNames =  batchNames + ","+ val.batch_name;
					});
				   batchNames = batchNames.replace(",","");
				   $("#studentDetailsClass").html(json.division.divisionName);
				   $("#studentDetailsBatch").html(batchNames);
				   $("#studentDetailsStudentPhone").html(json.studentUserBean.phone1);
				   $("#studentDetailsStudentEmail").html(json.studentUserBean.email);
				   $("#studentDetailsAddress").html(json.studentUserBean.addr1+","+json.studentUserBean.city+","+json.studentUserBean.state);
				   $("#studentDetailsParentName").html(json.student.parentFname+" "+json.student.parentLname);
				   $("#studentDetailsParentPhone").html(json.student.parentPhone);
				   $("#studentDetailsParentEmail").html(json.student.parentEmail);
			   	},
			   error:function(e){
			   
			   }
		}); */
		
		var handlers = {};

		handlers.success = function(data){console.log("Success",data);
		$("#studentDetailsBday").html(data.studentUserBean.dob.substring(6)+"/"+data.studentUserBean.dob.substring(4,6)+"/"+data.studentUserBean.dob.substring(0,4));
		   $("#studentDetailsName").html(data.studentUserBean.fname+" "+data.studentUserBean.lname);
		   var batchNames = "";
		   $.each(data.batches,function(key,val){
				batchNames =  batchNames + ","+ val.batch_name;
			});
		   batchNames = batchNames.replace(",","");
		   $("#studentDetailsClass").html(data.division.divisionName);
		   $("#studentDetailsBatch").html(batchNames);
		   $("#studentDetailsStudentPhone").html(data.studentUserBean.phone1);
		   $("#studentDetailsStudentEmail").html(data.studentUserBean.email);
		   $("#studentDetailsAddress").html(data.studentUserBean.addr1+","+data.studentUserBean.city+","+data.studentUserBean.state);
		   $("#studentDetailsParentName").html(data.student.parentFname+" "+data.student.parentLname);
		   $("#studentDetailsParentPhone").html(data.student.parentPhone);
		   $("#studentDetailsParentEmail").html(data.student.parentEmail);
		   if(data.studentUserBean.status == "M" || data.studentUserBean.status == "E"){
			 $("#generalTab").append("<div class='crendentialDetails'><div class='row'><label>Credential Information</label></div>"+
					 	"<div class='row'><div class='col-md-2'>Username</div>"+
				    	"<div class='col-md-1'>:</div>"+
				    	"<div class='col-md-3'>"+data.studentUserBean.loginName+"</div>"+
				    	"<div class='col-md-2'>Password</div>"+
				    	"<div class='col-md-1'>:</div>"+
				    	"<div class='col-md-3'>"+data.studentUserBean.loginPass+"</div></div>");  
		   }
		   createProgressCard(data.examWiseStudentDetails,data.batches);
		   createAttenanceTab(data.attendanceData);
		   createFeesTab(data.feesServiceBean,data.batches);
		$(".studentDetailsDiv").show();
		$("#marksGraphData").empty();
		if(graphData.length > 0){
		
			for (i =0; i <graphData.length;i++){
		if(graphData[i].length>0){
			$("#marksGraphData").append("<div id='graph"+i+"' style='border:outset;margin:1%'><div align='center' style='border-bottom:outset;font-weight:bold'>"+graphData[i][0].exam_name+"</div><div id='graphData"+i+"'></div></div>");
		}}}
		/* setTimeout(function(){
			
		},5000);
		 */
		$(".studentList").hide();
		}
		handlers.error = function(e){console.log("Error",e)}
		rest.get("rest/classownerservice/getStudentDetails/"+studentId,handlers);
	}
	function enableEdit(){
		enabledEdit = true;
		var batchData = getBatchesForStudent($(this));
		var classData = getAllClasses($(this));
		//var subjectName = $(this).closest("tr").find(".defaultteacherSuffix").text().trim();
		//$(this).closest("tr").find(".editteacherSuffix").val(subjectName);
		$(this).closest("tr").addClass("editEnabled");
	}


	function cancelEdit(){
		//$(this).closest("tr").find(".selectSubject").val(selectSubject).change();
		$(this).closest("tr").removeClass("editEnabled");
		$(this).closest("tr").find(".error").empty();
	}
	var that;
	function saveStudent(){
		$(this).closest("tr").find(".error").empty();
		//$(this).closest("tr").find(".suffixError").empty();
		var studentId = $(this).closest("tr").find("#studentId").val();
		//var teacherSuffix = $(this).closest("tr").find(".editteacherSuffix").val().trim();
		var batchIds = $(this).closest("tr").find(".selectBatch").val();
		that=$(this);
		updateStudentAjax(studentId,batchIds);
	}
	
	function updateStudentAjax(studentId,batchIds){
		var flag=false;
		var batchIdsStr = "";
		var divstr = that.closest("tr").find(".selectDivision").select2('data')[0].text;
		var div = that.closest("tr").find(".selectDivision").val();
		if(batchIds == "" || batchIds == null){
			flag = true;
			that.closest("tr").find(".batchError").html("Select Batch!");
		}else{
			 batchIdsStr = batchIds.join(',');	
		}
		
		if(flag==false){
		$.ajax({
				 url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "updateStudent",
						 regId:'',
						 batchIds:batchIdsStr,
						 studentId:studentId,
						 div:div
				   		},
				   type:"POST",
				   success:function(data){					  
					   that.closest("tr").find(".selectBatch").select2().val(batchIds).change();
					   var batchStr = "";
					   $.each(that.closest("tr").find(".selectBatch").select2('data'),function(key,val){
							batchStr =  batchStr + ","+ val.text;
						});
					   batchStr = batchStr.replace(",","");
					   that.closest("tr").find(".defaultBatchname").html(batchStr);
					   that.closest("tr").find(".defaultDiv").html(divstr);
					   that.closest("tr").removeClass("editEnabled");
				   	},
				   error:function(data){
				   
				   }
			});
		}
	}
	
	function deleteStudentPrompt(){
		var batchIdToDelete = $(this).closest("tr").find("#batchId").val();
		var studentId = $(this).closest("tr").find("#studentId").val();
		deleteBatchConfirm(studentId,$(this));	
	}
	
	function deleteBatchConfirm(studentId,that){
		modal.modalConfirm("Delete","Do you want to delete?","Cancel","Delete",deleteStudent,[studentId,that]);
	}
	
	function deleteStudent(studentId,that){
				/* $.ajax({
				 url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "deleteStudent",
						 regId:'',
						 studentId:studentId
				   		},
				   type:"POST",
				   success:function(data){
					   $.notify({message: "Student successfuly deleted"},{type: 'success'});
					   $('input[type="hidden"]#studentId[value="'+studentId+'"]').closest("tr").remove();
				   	},
				   error:function(data){
				
				   }
			}); */
		var handlers = {};
		handlers.success=function(){
			var table = $("#classTable").DataTable();
			that.closest("tr").addClass('selected');
			table.row('.selected').remove().draw( false );
			$.notify({message: "Student successfuly deleted"},{type: 'success'});
		};   
		handlers.error=function(){
			$.notify({message: "Student not deleted"},{type: 'danger'});
		};   
		
		rest.deleteItem("rest/commonDelete/deleteStudent/"+studentId,handlers);
	}
	
	function successCallbackclass(data,type){
		$(".containerData").show();
		data = JSON.parse(data);
		var status = data.status;
		$(".generateRollNumber").hide();
		if(status != "error"){
		if(type == "name"){
			dataTable = $('#classTable').DataTable({
				language: {
				        "emptyTable":     "Students not available"
				    },
				bDestroy:true,
				data: data.studentList,
				lengthChange: true,
				columns: [
					{title:"#",data:null},
					{ title: "Student Name",data:"studentUserBean",render:function(data,event,row){
						var input = "<input type='hidden' id='studentId' value='"+data.regId +"'>";
						var modifiedObj = data.fname+" "+data.lname;
						return modifiedObj+input;
					}},
					{ title: "Division",data:"division",render:function(data,event,row){
						/* console.log(row);
						var modifiedObj = data.divisionName;
						return modifiedObj; */
						var divisionNames = "";
						var selectTag = '<div class="editable"><select class="selectDivision" style="width:100%">';
						selectTag = selectTag+"</select></div>";
						var subjects;
						if(data != null){
						selectTag = selectTag + '<input type="hidden" class="editDivID" value="'+data.divId+'">';
						divisionNames = '<div class="default defaultDiv">'+data.divisionName+" "+data.stream+'</div>';
					}else{
						divisionNames = '<div class="default defaultDiv"></div>';
						selectTag = selectTag + '<input type="hidden" class="editDivID" value="-1">';
					}
						
						var span = '<span class="editable subjectError"></span>'
						return divisionNames + selectTag + span;
					}},
					{ title: "Batch",data:"batches",render:function(data,event,row){
						var batchNames = "";
						var selectTag = '<div class="editable"><select class="selectBatch" multiple="" style="width:100%">';
						var subjects;
						$.each(data,function(key,val){
							selectTag = selectTag + '<option selected="selected" value="'+val.batch_id+'">'+val.batch_name+'</option>';
							batchNames =  batchNames + ","+ val.batch_name;
						});
						batchNames = batchNames.replace(",","");
						batchNames = '<div class="default defaultBatchname">'+batchNames+'</div>';
						selectTag = selectTag+"</select></div>";
						var span = '<span class="editable batchError error"></span>'
						return batchNames + selectTag + span;
						}},
					{ title: "Actions",data:null,render:function(data){
						var buttons = '<div class="default">'+
						'<input type="button" class="btn btn-xs btn-primary btn-batch-edit" value="Edit">&nbsp;'+
						'<input type="button" class="btn btn-xs btn-danger btn-batch-delete" value="Delete">&nbsp;'+
						'<input type="button" class="btn btn-xs btn-info btn-student-details" value="Details">'+
					'</div>'+
					'<div class="editable">'+
						'<button class="btn btn-success btn-xs btn-save">Save</button>&nbsp;'+
						'<button class="btn btn-danger btn-xs btn-cancel">Cancel</button>'+
					'</div>'
					
					return buttons;
						}}
				]
			});
			
			dataTable.on( 'order.dt search.dt', function () {
	        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
	            cell.innerHTML = i+1;
				});
			}).draw();	
		}else{
			if(data.studentList.length>0){			$(".generateRollNumber").show();}
		dataTable = $('#classTable').DataTable({
			language: {
			        "emptyTable":     "Students not available"
			    },
			bDestroy:true,
			data: data.studentList,
			lengthChange: true,
			columns: [
				{title:"Roll no",data:"rollNo",render:function(data,event,row){
					if(data == 0){
						return  "-";	
					}else{
					return data;
					}
				}},
				{ title: "Student Name",data:"studentUserBean",render:function(data,event,row){
					var input = "<input type='hidden' id='studentId' value='"+data.regId +"'>";
					var modifiedObj = data.fname+" "+data.lname;
					return modifiedObj+input;
				}},
				{ title: "Division",data:"division",render:function(data,event,row){
					/* console.log(row);
					var modifiedObj = data.divisionName;
					return modifiedObj; */
					var divisionNames = "";
					var selectTag = '<div class="editable"><select class="selectDivision" style="width:100%">';
					selectTag = selectTag+"</select></div>";
					var subjects;
					if(data != null){
					selectTag = selectTag + '<input type="hidden" class="editDivID" value="'+data.divId+'">';
					divisionNames = '<div class="default defaultDiv">'+data.divisionName+" "+data.stream+'</div>';
				}else{
					divisionNames = '<div class="default defaultDiv"></div>';
					selectTag = selectTag + '<input type="hidden" class="editDivID" value="-1">';
				}
					
					var span = '<span class="editable subjectError"></span>'
					return divisionNames + selectTag + span;
				}},
				{ title: "Batch",data:"batches",render:function(data,event,row){
					var batchNames = "";
					var selectTag = '<div class="editable"><select class="selectBatch" multiple="" style="width:100%">';
					var subjects;
					$.each(data,function(key,val){
						selectTag = selectTag + '<option selected="selected" value="'+val.batch_id+'">'+val.batch_name+'</option>';
						batchNames =  batchNames + ","+ val.batch_name;
					});
					batchNames = batchNames.replace(",","");
					batchNames = '<div class="default defaultBatchname">'+batchNames+'</div>';
					selectTag = selectTag+"</select></div>";
					var span = '<span class="editable batchError error"></span>'
					return batchNames + selectTag + span;
					}},
				{ title: "Action",data:null,render:function(data){
					var buttons = '<div class="default">'+
					'<input type="button" class="btn btn-xs btn-primary btn-batch-edit" value="Edit">&nbsp;'+
					'<input type="button" class="btn btn-xs btn-danger btn-batch-delete" value="Delete">&nbsp;'+
					'<input type="button" class="btn btn-xs btn-info btn-student-details" value="Details">'+
				'</div>'+
				'<div class="editable">'+
					'<button class="btn btn-success btn-xs btn-save">Save</button>&nbsp;'+
					'<button class="btn btn-danger btn-xs btn-cancel">Cancel</button>&nbsp;'+
				'</div>'
				
				return buttons;
					}}
			]
		});
		
		/* dataTable.on( 'order.dt search.dt', function () {
        dataTable.column(0, {search:'applied', order:'applied'}).nodes().each( function (cell, i) {
            cell.innerHTML = i+1;
			});
		}).draw(); */
		}}else{
			that.closest('.addclassContainer').find(".addclassnameerror").html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong> Class already exists!!');
		}
	}
	var preselectedsubjects="";
	function getBatchesForStudent(that){
		preselectedsubjects=that.closest("tr").find(".selectBatch").val();
		$(".chkBatch:checked").removeAttr('checked');
		$('#checkboxes').children().remove();
		$('div#addStudentModal .error').hide();
		var divisionId = that.closest("tr").find(".editDivID").val();
		var batchDataArray = [];
		if(!divisionId || divisionId.trim()=="" || divisionId == -1){
			$('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Please select a division');
			$('div#addStudentModal .error').show();
			that.closest("tr").find(".selectBatch").select2({data:"",placeholder:"Select Batch"});
		}else{		
		  /* $.ajax({
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "fetchBatchesForDivision",
				 regId:'',
				 divisionId:divisionId						 
		   		},
		   type:"POST",
		   async:false,
		   success:function(e){
			    var data = JSON.parse(e);
			    if(data.status!="error"){
				var batchData = {};
			    $.each(data.batches,function(key,val){
					var data = {};
					data.id = val.batch_id;
					data.text = val.batch_name;
					batchDataArray.push(data);
					batchData[data.id] = val;
				});
			    }
		   	},
		   error:function(e){
			   $('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Error while fetching batches for division');
				$('div#addStudentModal .error').show();
		   }
		   
	}); */
	
			var handlers = {};
			handlers.success=function(data){
				 $.each(data,function(key,val){
						var data = {};
						data.id = val.batch_id;
						data.text = val.batch_name;
						batchDataArray.push(data);
						//batchData[data.id] = val;
					});
				console.log(batchDataArray);
				
					that.closest("tr").find(".selectBatch").select2({
				data:batchDataArray
			}).val(preselectedsubjects).change();
				return batchDataArray;
			};   
			handlers.error=function(){
				$.notify({message: "Error"},{type: 'danger'});
			};   
			if(divisionId != "-1"){
			rest.get("rest/classownerservice/getBatches/"+divisionId,handlers);
			}else{
				that.closest("tr").find(".selectBatch").select2({data:"",placeholder:"Select Batch"});
			}
	}
	}
	
	function getBatchesOfDivision(){
		$(".chkBatch:checked").removeAttr('checked');
		$('#checkboxes').children().remove();
		$('div#addStudentModal .error').hide();
		var divisionId = $('#division').val();
		var batchDataArray = [];
		if(!divisionId || divisionId.trim()=="" || divisionId == -1){
			$('#batch').empty();
			var tempData ={};
		    tempData.id = "-1";
		    tempData.text = "Select Batch";
		    $("#batch").select2({data:tempData,placeholder:"Select Batch"});
		}else{		
		  $.ajax({
		   url: "classOwnerServlet",
		   data: {
		    	 methodToCall: "fetchBatchesForDivision",
				 regId:'',
				 divisionId:divisionId						 
		   		},
		   type:"POST",
		   async:false,
		   success:function(e){
			   $('#batch').empty();
			    var data = JSON.parse(e);
			    if(data.status!="error"){
			    var tempData ={};
			    tempData.id = "-1";
			    tempData.text = "Select Batch";
			    batchDataArray.push(tempData);
				var batchData = {};
			    $.each(data.batches,function(key,val){
					var data = {};
					data.id = val.batch_id;
					data.text = val.batch_name;
					batchDataArray.push(data);
					batchData[data.id] = val;
				});
			    $("#batch").select2({data:batchDataArray,placeholder:"type batch name"}).data("batchData",batchData);
			    }else{
			    	$("#batch").select2({data:"",placeholder:"No batch found"});
			    }
		   	},
		   error:function(e){
			   $('div#addStudentModal .error').html('<i class="glyphicon glyphicon-warning-sign"></i> <strong>Error!</strong>Error while fetching batches for division');
				$('div#addStudentModal .error').show();
		   }
		   
	});

	}
	}

	function getNames(){
		var names = {};
		  $.ajax({
			   url: "classOwnerServlet",
			   data: {
			    	 methodToCall: "fetchNamesForSuggestion"						 
			   		},
			   type:"POST",
			   async : false,
			   success:function(e){
				   var data = JSON.parse(e);
				   names =  data.names;
			   },
			   error:function(e){
				   }
			   });
		return names;
	}
	
	function getAllClasses(that){
		var classDataArray = [];
		var classData = {};
		/* $.ajax({
				 url: "classOwnerServlet",
				   data: {
				    	 methodToCall: "getAllClasses"
				   		},
				   type:"POST",
					async:false,
				   success:function(data){
						classData = JSON.parse(data);
				   	},
				   error:function(data){
				   
				   }
			}); */
		var handlers = {};
		handlers.success=function(e){
			$.each(e,function(data,val){
				var data = {};
				data.id = val.divId;
				data.text = val.divisionName+" "+val.stream;
				classDataArray.push(data);
			});
			console.log(classDataArray);
			if(classDataArray.length > 0){
			 var preselectedclass=that.closest("tr").find(".editDivID").val();
				that.closest("tr").find(".selectDivision").select2({
					data:classDataArray
				}).val(preselectedclass).change(); 
				//that.closest("tr").find(".selectDivision").trigger("change");
			}else{
				that.closest("tr").find(".selectDivision").select2({data:"",placeholder:"No Class found"});
			}
			return classDataArray;
		};   
		handlers.error=function(){
			$.notify({message: "Error"},{type: 'danger'});
		};   
		
		rest.get("rest/classownerservice/getAllClasses",handlers);
			
			
	}
</script>

<body>
<div class="studentList">
<ul class="nav nav-tabs" style="border-radius:10px">
  <li><a href="managestudent">Add Student</a></li>
   <li><a href="bulkStudentUpload">Add Student Through File</a></li>
  <li class="active"><a href="#viewstudenttab" data-toggle = "tab">View Student</a></li>
</ul>

<div id="viewstudenttab">
<div class="well">
<div class="row searchStudent">
<div class="col-md-3">
	<select id="division" class="form-control">
		<option value="-1">Select Class</option>
		<c:forEach items="${divisions}" var="division">
			<option value=<c:out value='${division.divId }'></c:out>><c:out value="${division.divisionName }"></c:out> <c:out value="${division.stream }"></c:out></option>
		</c:forEach>
	</select>
	<span class="error" id="divisionError"></span>
</div>
<div class="col-md-3">
<select id="batch" style="width:100%" class="form-control">
	<option value="-1">Select Batch</option>
</select>
<span class="error" id="batchError"></span>
</div>
<div class="col-md-2">
<button class="btn btn-primary searchStudentByBatch" id="searchStudentByBatch">Search</button>
</div>
	<div class="col-md-1"><button class="btn btn-primary" style="border-radius:55%;background: grey;border: grey" disabled="disabled">OR</button></div>
<div class="col-md-3">
<form action="javascript:void(0)">
        <div class="typeahead-container">
            <div class="typeahead-field">

            <span class="typeahead-query">
                <input id="studentNameSearch"
                       name="studentNameSearch"
                       type="search"
                       autofocus
                       autocomplete="off" Placeholder="Search By Name" class="form-control" style="border-radius:4px 0 0 4px">
            </span>
            <span class="typeahead-button">
                <button type="submit" id="searchStudentByName" style="border-radius:0 4px 4px 0">
                    <span class="typeahead-search-icon" ></span>
                </button>
            </span>

            </div>
        </div>
    </form>
</div>
</div>
</div>
</div>
<div class="container containerData">
<button class="btn btn-primary generateRollNumber hide">Generate Roll No</button>
<br/><br/>
<table class="table table-striped classTable" id="classTable" >
	<thead>
		<th>Roll no</th><th>Student name</th><th>Division</th><th>Batch</th><th></th>
	</thead>
	<tbody></tbody>
</table>
</div>
<form action="studentDetails" class="studentDetails">
<input type="hidden" id="studentID" name="studentID">
<input type="hidden" id="batchID" name="batchID">
<input type="hidden" id="divisionID" name="divisionID">
<input type="hidden" id="currentPage" name="currentPage">
</form>
</div>
<div class="studentDetailsDiv" style="display: none">
<div class="container">
<div class="row">
<button class="btn btn-primary" id="back">Back To Student List</button>
</div>
</div>
<div class="container" style="padding-top: 2%">
<div class="row">
	<div class="col-md-4">Name : <span id="studentDetailsName"></span></div>
	<div class="col-md-3">Class : <span id="studentDetailsClass"></span></div>
	<div class="col-md-3">Batch : <span id="studentDetailsBatch"></span></div>
</div>
<div class="row" style="padding-top: 1%">
<ul class="nav nav-tabs" style="border-radius:10px">
  <li class="active"><a href="#generalTab" data-toggle = "tab">General</a></li>
  <li><a href="#marksTab" data-toggle = "tab">Marks</a></li>
  <li><a href="#feesTab" data-toggle = "tab">Fees</a></li>
  <li><a href="#attendanceTab" data-toggle = "tab">Attendance</a></li>
</ul>
</div>
<div class="tab-content" style="padding-top: 1%">
  <div id="generalTab" class="tab-pane fade in active">
   <div class="row"><label>Student Information</label></div>
    <div class="row">
    	<div class="col-md-2">Birth Date</div>
    	<div class="col-md-1">:</div>
    	<div class="col-md-3"><span id="studentDetailsBday"></span></div>
    	<div class="col-md-2">Phone No</div>
    	<div class="col-md-1">:</div>
    	<div class="col-md-3"><span id="studentDetailsStudentPhone"></span></div>
    </div>
    <div class="row">
    	<div class="col-md-2">Email ID</div>
    	<div class="col-md-1">:</div>
    	<div class="col-md-3"><span id="studentDetailsStudentEmail"></span></div>
    	<div class="col-md-2">Address</div>
    	<div class="col-md-1">:</div>
    	<div class="col-md-3"><span id="studentDetailsAddress"></span></div>
    </div>
    <div class="row"><label>Parents Information</label></div>
    <div class="row">
    	<div class="col-md-2">Parent Name</div>
    	<div class="col-md-1">:</div>
    	<div class="col-md-3"><span id="studentDetailsParentName"></span></div>
    	<div class="col-md-2">Phone No</div>
    	<div class="col-md-1">:</div>
    	<div class="col-md-3"><span id="studentDetailsParentPhone"></span></div>
    </div>
    <div class="row">
    	<div class="col-md-2">Email ID</div>
    	<div class="col-md-1">:</div>
    	<div class="col-md-3"><span id="studentDetailsParentEmail"></span></div>
    </div>
  </div>
  <div id="marksTab" class="tab-pane fade marksTab">
    <div id="marksTabData"></div>
    <div id="marksGraphData"></div>
  </div>
  <div id="feesTab" class="tab-pane fade">
    <h3>Fees</h3>
  </div>
  <div id="attendanceTab" class="tab-pane fade">
  </div>
</div>
</div>
</div>
</body>
</html>
		
