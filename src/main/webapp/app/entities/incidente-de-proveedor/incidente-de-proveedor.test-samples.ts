import dayjs from 'dayjs/esm';

import { IIncidenteDeProveedor, NewIncidenteDeProveedor } from './incidente-de-proveedor.model';

export const sampleWithRequiredData: IIncidenteDeProveedor = {
  id: 30015,
  estado: 'RESUELTO',
  abiertoEn: dayjs('2023-12-04T15:10'),
};

export const sampleWithPartialData: IIncidenteDeProveedor = {
  id: 17725,
  ticketExterno: 'punctual mortally director',
  estado: 'RECHAZADO',
  abiertoEn: dayjs('2023-12-04T17:07'),
  primeraRespuestaEn: dayjs('2023-12-04T17:39'),
  cumplioSla: false,
  motivoRechazo: 'sweetly because',
  responsableResuelto: 'yak suburban',
};

export const sampleWithFullData: IIncidenteDeProveedor = {
  id: 10426,
  ticketExterno: 'vacantly now',
  estado: 'RESUELTO',
  abiertoEn: dayjs('2023-12-04T15:50'),
  primeraRespuestaEn: dayjs('2023-12-04T03:15'),
  resueltoEn: dayjs('2023-12-04T06:28'),
  cumplioSla: true,
  motivoRechazo: 'appropriate lest scarcely',
  responsableResuelto: 'rundown',
  nivelResuelto: 'PRIMARIO',
  huboCobertura: true,
  notas: 'bah',
};

export const sampleWithNewData: NewIncidenteDeProveedor = {
  estado: 'ESPERANDO_PROVEEDOR',
  abiertoEn: dayjs('2023-12-04T11:10'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
