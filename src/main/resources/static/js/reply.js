// 댓글 수정
function editReply(button) {

    const replyItem = button.closest(".reply-item");

    const content = replyItem.querySelector(".reply-content");
    const editForm = replyItem.querySelector(".reply-edit-form");
    const action = replyItem.querySelector(".reply-action");


    content.style.display = "none";
    editForm.style.display = "block";
    action.style.display = "none";
}


function cancelReplyEdit(button) {

    const replyItem = button.closest(".reply-item");

    const content = replyItem.querySelector(".reply-content");
    const editForm = replyItem.querySelector(".reply-edit-form");
    const action = replyItem.querySelector(".reply-action");


    content.style.display = "block";
    editForm.style.display = "none";
    action.style.display = "flex";
}