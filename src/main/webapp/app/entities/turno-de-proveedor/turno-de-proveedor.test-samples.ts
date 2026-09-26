import dayjs from 'dayjs/esm';

import { ITurnoDeProveedor, NewTurnoDeProveedor } from './turno-de-proveedor.model';

export const sampleWithRequiredData: ITurnoDeProveedor = {
  id: 13553,
  desde: dayjs('2023-12-04T09:17'),
  hasta: dayjs('2023-12-04T19:36'),
  nivel: 'SECUNDARIO',
  esReemplazo: true,
};

export const sampleWithPartialData: ITurnoDeProveedor = {
  id: 24117,
  desde: dayjs('2023-12-04T01:06'),
  hasta: dayjs('2023-12-04T06:14'),
  nivel: 'SECUNDARIO',
  esReemplazo: false,
};

export const sampleWithFullData: ITurnoDeProveedor = {
  id: 31653,
  desde: dayjs('2023-12-04T01:44'),
  hasta: dayjs('2023-12-04T10:08'),
  nivel: 'PRIMARIO',
  esReemplazo: false,
  nota: 'as instantly accidentally',
};

export const sampleWithNewData: NewTurnoDeProveedor = {
  desde: dayjs('2023-12-04T22:33'),
  hasta: dayjs('2023-12-04T01:08'),
  nivel: 'PRIMARIO',
  esReemplazo: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
