import { IImage } from 'app/shared/model/image.model';

export interface IWarehouse {
    id?: number;
    name?: string;
    description?: string;
    tel?: string;
    imageId?: number;
    locationId?: number;
    image?: IImage;
}

export class Warehouse implements IWarehouse {
    constructor(
        public id?: number,
        public name?: string,
        public description?: string,
        public tel?: string,
        public imageId?: number,
        public locationId?: number,
        public image?: IImage
    ) {}
}
