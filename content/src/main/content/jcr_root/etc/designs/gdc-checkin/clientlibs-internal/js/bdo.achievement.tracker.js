GDC.bdo.achievement={};


GDC.bdo.achievement.tracker = function(bdoAchieved) {
	
	 if(GDC.bdo.isEmpty(bdoAchieved)) {
		 bdoAchieved = 0;
      }
     else {
    	 bdoAchieved = parseInt(bdoAchieved);
     }
	
	var bdoYettoAchieve = 100 - bdoAchieved;

	var s1 = [['Achieved',bdoAchieved], ['Yet to Achieve',bdoYettoAchieve]]; 

	var data = [s1];

    var options = {
        seriesDefaults: {
                // make this a donut chart.
                renderer:$.jqplot.DonutRenderer,
                rendererOptions:{
                    // Donut's can be cut into slices like pies.
                    sliceMargin: 3,
                    // Pies and donuts can start at any arbitrary angle.
                    startAngle: -90,
                    showDataLabels: true,
                    // By default, data labels show the percentage of the donut/pie.
                    // You can show the data 'value' or data 'label' instead.
                    dataLabels: 'percent'
                },
                seriesColors: ["#008837","#0571B0"]
              },
              grid: {
                drawGridLines: false,        // wether to draw lines across the grid or not.
                gridLineColor: '#fff',   // CSS color spec of the grid lines.
                background: '#fff',      // CSS color spec for background color of grid.
                borderColor: '#fff',     // CSS color spec for border around grid.
                borderWidth: 2.0,           // pixel width of border around grid.
                shadow: false,               // draw a shadow for grid.
                shadowAngle: 45,            // angle of the shadow.  Clockwise from x axis.
                shadowOffset: 1.5,          // offset from the line of the shadow.
                shadowWidth: 0,             // width of the stroke for the shadow.
                shadowDepth: 0
              }, 
              legend: {show:true,location:'s'}
      };

    $.jqplot('achievementChart', data, options);

}


