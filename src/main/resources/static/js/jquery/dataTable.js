$(document).ready(function() {
	$('#tabla-alquiler').DataTable({
		"language": { url: '//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json' },
		"responsive": true,
		"order": [[0, "asc"]]
	});
	$('#tabla-pago').DataTable({
		"language": { url: '//cdn.datatables.net/plug-ins/1.10.15/i18n/Spanish.json' },
		"responsive": true,
		"order": [[0, "asc"]]
	});
});