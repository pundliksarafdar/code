var str1 = '{"home": "home.html", "dealer": "dealer.html", "nearest": "nearest.html", "catlogue": "catlogue.html", "contact": "contact.html", "help": "help.html"}';
var currentHash;
var hash;
	
function Router(division,hashPagesJson){
		this.hashPagesJson = hashPagesJson;
		this.division = division;
}

Router.prototype.startRouter = function(){
	var str = this.hashPagesJson;
	var t = JSON.parse(str);
	var that = this;
	setTimeout(function(){
		hash = (window.location.hash).substring(1);
			if(currentHash !== hash){
				currentHash = hash;	
				if(t[hash] !== undefined){
					$("#content").load(t[hash]);
				}else{
					window.location.hash = "#home";
				}
			}
		that.startRouter();
	},100);
}

var router = new Router('div',str1);
router.startRouter();