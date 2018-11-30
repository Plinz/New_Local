import { IImage } from 'app/shared/model//image.model';
import { ILocation } from 'app/shared/model//location.model';

export interface IWarehouse {
    id?: number;
    name?: string;
    description?: string;
    tel?: string;
    image?: IImage;
    location?: ILocation;
}

export class Warehouse implements IWarehouse {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public tel?: string,
        public image?: IImage,
        public location?: ILocation
    ) {}
}
