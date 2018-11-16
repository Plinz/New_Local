/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { NewLocalTestModule } from '../../../test.module';
import { PurchaseDeleteDialogComponent } from 'app/entities/purchase/purchase-delete-dialog.component';
import { PurchaseService } from 'app/entities/purchase/purchase.service';

describe('Component Tests', () => {
    describe('Purchase Management Delete Component', () => {
        let comp: PurchaseDeleteDialogComponent;
        let fixture: ComponentFixture<PurchaseDeleteDialogComponent>;
        let service: PurchaseService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NewLocalTestModule],
                declarations: [PurchaseDeleteDialogComponent]
            })
                .overrideTemplate(PurchaseDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PurchaseDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PurchaseService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
