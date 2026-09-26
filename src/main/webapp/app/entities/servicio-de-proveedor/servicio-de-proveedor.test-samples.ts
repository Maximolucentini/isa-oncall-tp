import { IServicioDeProveedor, NewServicioDeProveedor } from './servicio-de-proveedor.model';

export const sampleWithRequiredData: IServicioDeProveedor = {
  id: 26256,
  nombre: 'unused',
  activo: true,
};

export const sampleWithPartialData: IServicioDeProveedor = {
  id: 28293,
  nombre: 'hm yum',
  identificadorExterno: 'petticoat edible',
  slaRespuestaMinutos: 6108,
  activo: false,
};

export const sampleWithFullData: IServicioDeProveedor = {
  id: 12092,
  nombre: 'bah drag frail',
  identificadorExterno: 'doodle accentuate',
  descripcion: 'merrily yahoo furthermore',
  slaRespuestaMinutos: 1333,
  activo: true,
};

export const sampleWithNewData: NewServicioDeProveedor = {
  nombre: 'staid outstanding',
  activo: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
