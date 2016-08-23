if(location.protocol == "http:" && location.hostname!="localhost"){
	window.location.href = "https://"+window.location.href.substring(7);
}
console.log("Need to remove the code here.....urgently");