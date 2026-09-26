import dayjs from 'dayjs/esm';

import { EstadoTicketProveedor } from 'app/entities/enumerations/estado-ticket-proveedor.model';
import { NivelGuardia } from 'app/entities/enumerations/nivel-guardia.model';
import { IIncidente } from 'app/entities/incidente/incidente.model';
import { IServicioDeProveedor } from 'app/entities/servicio-de-proveedor/servicio-de-proveedor.model';
import { IUser } from 'app/entities/user/user.model';

export interface IIncidenteDeProveedor {
  id: number;
  ticketExterno?: string | null;
  estado?: keyof typeof EstadoTicketProveedor | null;
  abiertoEn?: dayjs.Dayjs | null;
  primeraRespuestaEn?: dayjs.Dayjs | null;
  resueltoEn?: dayjs.Dayjs | null;
  cumplioSla?: boolean | null;
  motivoRechazo?: string | null;
  responsableResuelto?: string | null;
  nivelResuelto?: keyof typeof NivelGuardia | null;
  huboCobertura?: boolean | null;
  notas?: string | null;
  incidente?: Pick<IIncidente, 'id' | 'titulo'> | null;
  servicioDeProveedor?: Pick<IServicioDeProveedor, 'id' | 'nombre'> | null;
  abiertoPor?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewIncidenteDeProveedor = Omit<IIncidenteDeProveedor, 'id'> & { id: null };
