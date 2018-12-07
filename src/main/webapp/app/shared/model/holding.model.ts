import { IImage } from 'app/shared/model/image.model';

export interface IHolding {
    id?: number;
    siret?: string;
    name?: string;
    description?: string;
    imageId?: number;
    locationId?: number;
    ownerId?: number;
    image?: IImage;
}

export class Holding implements IHolding {
    constructor(
        public id?: number,
        public siret?: string,
        public name?: string,
        public description?: string,
        public imageId?: number,
        public locationId?: number,
        public ownerId?: number,
        public image?: IImage
    ) {}
}
