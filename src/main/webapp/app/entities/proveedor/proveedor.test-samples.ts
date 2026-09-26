import { IProveedor, NewProveedor } from './proveedor.model';

export const sampleWithRequiredData: IProveedor = {
  id: 1215,
  nombre: 'speedily parody till',
  tipo: 'OTRO',
  zonaHoraria: 'forenenst',
  cobertura: 'HORARIO_HABIL',
  activo: true,
};

export const sampleWithPartialData: IProveedor = {
  id: 6996,
  nombre: 'platter',
  tipo: 'CDN',
  zonaHoraria: 'serve',
  emailContacto: 'onset gadzooks',
  urlSoporte: 'defiantly ah manner',
  cobertura: 'HORARIO_HABIL',
  activo: false,
};

export const sampleWithFullData: IProveedor = {
  id: 2129,
  nombre: 'hence contravene those',
  tipo: 'OTRO',
  zonaHoraria: 'SUV punctually last',
  telefonoContacto: 'once near',
  emailContacto: 'wetly flu',
  urlSoporte: 'grok despite pfft',
  urlEstado: 'cassava which',
  cobertura: 'SOLO_CRITICO',
  slaRespuestaMinutos: 495,
  activo: false,
};

export const sampleWithNewData: NewProveedor = {
  nombre: 'provided',
  tipo: 'OBSERVABILIDAD',
  zonaHoraria: 'unnecessarily barracks',
  cobertura: 'SOLO_CRITICO',
  activo: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
