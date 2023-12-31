/**
 * 取得表格分頁導覽列
 *
 * @param {Object} response 資料回覆物件
 */
function getPolicyPageBarHtml(response) {
    var pageInfoHtml = '';
    var prevHtml = '<li class="disabled"><a href="#" aria-label="Previous"><span aria-hidden="true">上一頁</span></a></li>';
    var nextHtml = '<li class="disabled"><a href="#" aria-label="next"><span aria-hidden="true">下一頁</span></a></li>';
    if ($(response.rows).size() == 0) {
        pageInfoHtml += prevHtml;
        pageInfoHtml += '<li class=""><a href="#">1 <span class="sr-only">(current)</span></a></li>';
        pageInfoHtml += nextHtml;
    } else {
        var totalPageNumSize = $(response.pageNumList).size();
        // 上一頁
        var currentPage = response.pageNum;
        if (response.prev) {
            firstHtml = '<li class=""><a style="border:0; " href="javascript:queryDataByPage(' + ($(response.pageNumList).get(0)) + ');" aria-label="Previous"><span style="color: #CC7A00;" aria-hidden="true">第一頁</span></a></li>';
            pageInfoHtml += firstHtml;
            prevHtml = '<li class=""><a href="javascript:queryDataByPage(' + (currentPage - 1) + ');" aria-label="Previous"><span aria-hidden="true">上一頁</span></a></li>';
            pageInfoHtml += prevHtml;
        }

        // 頁數清單 (控制頁數顯示不要過多)
        var pageDisplayMinNum = 5; // 設定奇數
        var pageCtrlNum = parseInt(pageDisplayMinNum / 2);
        if (totalPageNumSize <= pageDisplayMinNum) {
            // 若總頁數小於預設，則正常顯示所有頁數
            $.each(response.pageNumList, function(j, pageNumber) {
                if (currentPage == pageNumber) {
                    pageInfoHtml += '<li class="active"><a href="#">';
                } else {
                    pageInfoHtml += '<li class=""><a href="javascript:queryDataByPage(' + pageNumber + ');">';
                }
                pageInfoHtml += (pageNumber + ' <span class="sr-only">(current)</span></a></li>');
            });
        } else {
            if (currentPage <= pageCtrlNum) {
                for (var i = 1; i <= pageDisplayMinNum; i++) {
                    if (currentPage == i) {
                        pageInfoHtml += '<li class="active"><a href="#">';
                    } else {
                        pageInfoHtml += '<li class=""><a href="javascript:queryDataByPage(' + i + ');">';
                    }
                    pageInfoHtml += (i + ' <span class="sr-only">(current)</span></a></li>');
                }              
            } else if (currentPage > pageCtrlNum && (totalPageNumSize - pageCtrlNum) > currentPage) {
                // 顯示當前頁在中間
                for (var i = 0; i < pageCtrlNum; i++) {
                    pageInfoHtml += '<li class=""><a href="javascript:queryDataByPage(' + (currentPage - pageCtrlNum + i) + ');">';
                    pageInfoHtml += ((currentPage - pageCtrlNum + i) + ' <span class="sr-only">(current)</span></a></li>');
                }
                
                pageInfoHtml += '<li class="active"><a href="#">';
                pageInfoHtml += (currentPage + ' <span class="sr-only">(current)</span></a></li>');
                
                for (var i = 1; i <= pageCtrlNum; i++) {
                    pageInfoHtml += '<li class=""><a href="javascript:queryDataByPage(' + (currentPage + i) + ');">';
                    pageInfoHtml += ((currentPage + i) + ' <span class="sr-only">(current)</span></a></li>');
                }
            } else if (currentPage >= (totalPageNumSize - pageDisplayMinNum) && currentPage <= totalPageNumSize) {
                for (var i = (totalPageNumSize - pageDisplayMinNum + 1); i <= totalPageNumSize; i++) {
                    if (currentPage == i) {
                        pageInfoHtml += '<li class="active"><a href="#">';
                    } else {
                        pageInfoHtml += '<li class=""><a href="javascript:queryDataByPage(' + i + ');">';
                    }
                    pageInfoHtml += (i + ' <span class="sr-only">(current)</span></a></li>');
                } 
            }
        }
        
        // 下一頁
        if (response.next) {
            nextHtml = '<li><a href="javascript:queryDataByPage(' + (currentPage + 1) + ');" aria-label="next"><span aria-hidden="true">下一頁</span></a></li>';
            pageInfoHtml += nextHtml;
            endHtml = '<li><a href="javascript:queryDataByPage(' + ($(response.pageNumList).get($(response.pageNumList).size() -1 )) + ');" aria-label="next"><span style="color: #CC7A00; aria-hidden="true">最未頁</span></a></li>';
            pageInfoHtml += endHtml;
        }

        pageInfoHtml += '<li style="position: relative; float: left; padding: 4px 10px; margin-left: -1px; line-height: 1.42857143; text-decoration: none; background-color: #fff;">總共' + totalPageNumSize + '頁</li>'
    }
    
    return pageInfoHtml;
}
