import { ILocation } from 'app/shared/model//location.model';
import { IImage } from 'app/shared/model//image.model';

export interface IWarehouse {
    id?: number;
    name?: string;
    description?: string;
    tel?: string;
    location?: ILocation;
    images?: IImage[];
}

export class Warehouse implements IWarehouse {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public tel?: string,
        public location?: ILocation,
        public images?: IImage[]
    ) {}
}
