var homeData = [{TabName:"Class owner",TabIcon:"",active:true,
					features:[{color:"rgba(247,247,247,0.93)",text:"feature 1",imageUrl:"/images/class.svg",featureDetails:"Details goes here",iconHeight:"100px",iconWidth:"100px"},
					          {color:"rgba(247,247,247,0.93)",text:"feature 2",imageUrl:"/images/class.svg",featureDetails:"Details goes here",iconHeight:"100px",iconWidth:"100px"},
					          {color:"rgba(247,247,247,0.93)",text:"feature 3",imageUrl:"/images/class.svg",featureDetails:"Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here Details  goes here ",iconHeight:"100px",iconWidth:"100px"},
					          {color:"rgba(247,247,247,0.93)",text:"feature 4",imageUrl:"/images/class.svg",featureDetails:"Details goes here",iconHeight:"100px",iconWidth:"100px"},
					          {color:"rgba(247,247,247,0.93)",text:"feature 5",imageUrl:"/images/class.svg",featureDetails:"Details goes here",iconHeight:"100px",iconWidth:"100px"},
					          {color:"rgba(247,247,247,0.93)",text:"feature 5",imageUrl:"/images/class.svg",featureDetails:"Details goes here",iconHeight:"100px",iconWidth:"100px"},
					          {color:"rgba(247,247,247,0.93)",text:"feature 6",imageUrl:"/images/class.svg",featureDetails:"Details goes here",iconHeight:"50px"}]
				},
                {TabName:"Teacher",TabIcon:"",
                	features:[{color:"rgba(247,247,247,0.93)",text:"feature 1",imageUrl:"/images/class.svg",featureDetails:"Details goes here",iconHeight:"200px",iconWidth:"100px"},
					          {color:"rgba(247,247,247,0.93)",text:"feature 2",imageUrl:"/images/class.svg",featureDetails:"Details goes here",iconHeight:"300px",iconWidth:"100px"},
					          ]},
                {TabName:"Student",TabIcon:""},
                {TabName:"Parents",TabIcon:""}]

var settings = {label:{color:"maroon",size:"24px"},detail:{color:"gray",size:"12px"}};

function showDataModal(){
	modal.launchAlert($(this).attr("heading"),$(this).text());
}
$.fn.makeTabs = function(){
	
	$(window).resize(function () {
        if (this.resizeTO) clearTimeout(this.resizeTO);
        this.resizeTO = setTimeout(function () {
            $(this).trigger('resizeEnd');
        }, 10);
    });
	
	$(window).bind('resizeEnd', function () {
        /*
		$(".tile").height($("#tile").width());
        //$(".carousel").height($("#tile1").width());
        $(".item").height($("#tile").width());
        */
    });
	
	var headTabList = $("<ul/>",{class:"nav nav-tabs nav-justified"});
	var headTabsContent = $("<div/>",{class:"tab-content"});
	$.each(homeData,function(){
		var a = $("<a/>",{text:this.TabName,"data-toggle":"tab",href:"#"+this.TabName.replace(" ","")});
		var li = $("<li/>",{id:"home-features-tab-homeTabs"}).append(a);
		var div = $("<div/>",{class:"tab-pane",id:this.TabName.replace(" ","")});
		div.append("<br/>");
		var features = this.features;
		if(features){
			$.each(features,function(){
				var tiles = '<div class="tileHead col-lg-3 col-md-4 col-sm-6"><div id="tile" class="tile"><div class="carousel-inner"><div class="item active text-center">'+
	                            '<div id="imagePlaceholder"></div><label id="textHead">Student</label></div>'+
	                            '<div><p id="featureDetails"></p></div>'+
	                            '</div></div></div>';

					var feature = this;
				
					var icon;
					var $tiles = $(tiles);
					if(feature.icons){
						icon = $("<img/>",{src:feature.icons});
					}else{
						icon = $("<img/>",{src:feature.imageUrl}).css({width:feature.iconWidth,height:feature.iconHeight});
					}
					
					if(icon){
						$tiles.find("#imagePlaceholder").append(icon);
						div.append($tiles);
					}
					
					if(feature.color){
						$tiles.find("#tile").css({"background":feature.color});
					}
					
					if(feature.text){
						$tiles.find("#textHead").text(feature.text).css({"font-size":settings.label.size,color:settings.label.color});
					}
					
					if(feature.featureDetails){
						$tiles.find("p#featureDetails").text(feature.featureDetails).attr("heading",feature.text).on("click",showDataModal).css({"font-size":settings.detail.size,color:settings.detail.color});
					}
					
			});
		}
		if(this.active){
			li.addClass("active");
			div.addClass("active");
		}
		headTabList.append(li);
		headTabsContent.append(div);
	});
	
	
	$(this).append(headTabList);
	$(this).append(headTabsContent);
	$(this).find("#home-features-tab-homeTabs a").click(function (e) {
		  e.preventDefault();
		  $(this).tab('show');
		});
		
		/**************************************************************/
		/*
		$(".tile").height($("#tile").width());
	    $(".item").height($("#tile").width());
	    */
		/**************************************************************/
	
}