function iniciarJob(id) {
	$.ajax({
		url: '/schedulerJobInfo/iniciarJob',
		type: 'get',
		data: {
			jobId: id
		},
		success: function(response) {
			alert('Proces execute');
		},
		error: function(xhr) {
			alert('Error, proces not execute');
		}
	});
}

$('.btn-edit').click(function(){
    var $modal = $('#editModalTemplate').clone().removeAttr('id');

    var $btn = $(this);
    var id = $btn.attr('data-id');
    var cronExpression = $btn.attr('data-cronExpression');
    var jobName = $btn.attr('data-name');

    $modal.find('[data-value="id"]').text(id);
    $modal.find('[data-value="cronExpression"]').text(cronExpression);
    $modal.find('[data-value="jobName"]').text(jobName);
    
    $modal.find('[name="id"]').val($btn.attr('data-id'));
    $modal.find('[name="jobName"]').val($btn.attr('data-jobName'));
    $modal.find('[name="cronExpression"]').val($btn.attr('data-cronExpression'));

    $modal.modal();
});